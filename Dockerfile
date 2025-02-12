FROM mcr.microsoft.com/dotnet/runtime:7.0

ENV STABLE_URL="https://cdn.vintagestory.at/gamefiles/stable/vs_server_linux-x64_"
ENV UNSTABLE_URL="https://cdn.vintagestory.at/gamefiles/unstable/vs_server_linux-x64_"

# Install required packages
RUN apt-get update && apt-get install -y \
    wget jq \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir /game
VOLUME /data

WORKDIR /game

# Copy scripts into the container
COPY scripts/download_server.sh /game
COPY scripts/check_and_start.sh /game

# Make scripts executable and set ownership
RUN chmod +x /game/*.sh

EXPOSE 42420

ENTRYPOINT ["/game/check_and_start.sh"]
