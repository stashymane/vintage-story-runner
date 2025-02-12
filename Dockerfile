FROM mcr.microsoft.com/dotnet/runtime:7.0

VOLUME /srv/gameserver/data/vs

ENV STABLE_URL="https://cdn.vintagestory.at/gamefiles/stable/vs_server_linux-x64_"
ENV UNSTABLE_URL="https://cdn.vintagestory.at/gamefiles/unstable/vs_server_linux-x64_"

# Install required packages
RUN apt-get update && apt-get install -y \
    wget jq \
    && rm -rf /var/lib/apt/lists/*

# Create non-root user and group with specific IDs
RUN useradd -u 1000 -m -s /bin/bash gameserver

# Create necessary directories
RUN mkdir -p /srv/gameserver/vintagestory

# Set ownership
RUN chown -R gameserver:gameserver /srv/gameserver

WORKDIR /srv/gameserver/vintagestory

# Copy scripts into the container
COPY scripts/download_server.sh /srv/gameserver/vintagestory/
COPY scripts/check_and_start.sh /srv/gameserver/vintagestory/

# Make scripts executable and set ownership
RUN chmod +x /srv/gameserver/vintagestory/*.sh && \
    chown gameserver:gameserver /srv/gameserver/vintagestory/*.sh

USER gameserver

EXPOSE 42420

ENTRYPOINT ["/srv/gameserver/vintagestory/check_and_start.sh"]
