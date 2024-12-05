FROM mcr.microsoft.com/dotnet/runtime:7.0

# Install required packages
RUN apt-get update && apt-get install -y \
    wget jq \
    && rm -rf /var/lib/apt/lists/*

# Create non-root user and group with specific IDs
RUN useradd -u 1000 -m -s /bin/bash gameserver

# Create necessary directories
RUN mkdir -p /srv/gameserver/vintagestory \
    /srv/gameserver/data/vs

# Set ownership 
RUN chown -R gameserver:gameserver /srv/gameserver && \
    chmod -R 755 /srv/gameserver

WORKDIR /srv/gameserver/vintagestory

USER gameserver

# Add startup script
RUN echo '#!/bin/bash\n\
\n\
if [ -z "$VERSION" ]; then\n\
    echo "ERROR: VERSION environment variable must be set"\n\
    exit 1\n\
fi\n\
\n\
if [ ! -f VintagestoryServer.dll ]; then\n\
    STABLE_URL="https://cdn.vintagestory.at/gamefiles/stable/vs_server_linux-x64_${VERSION}.tar.gz"\n\
    UNSTABLE_URL="https://cdn.vintagestory.at/gamefiles/unstable/vs_server_linux-x64_${VERSION}.tar.gz"\n\
    \n\
    # Try stable URL first\n\
    if wget --spider -q "$STABLE_URL" 2>/dev/null; then\n\
        DOWNLOAD_URL="$STABLE_URL"\n\
        echo "Downloading Vintage Story Server version ${VERSION} from stable..."\n\
        \n\
        # Check latest stable version\n\
        STABLE_JSON=$(wget -qO- https://api.vintagestory.at/stable.json)\n\
        LATEST_STABLE=$(echo "$UNSTABLE_JSON" | jq -r 'keys_unsorted[0]')\n\
        if [ "$VERSION" != "$LATEST_STABLE" ]; then\n\
            echo "NOTE: You are running stable version ${VERSION} but version ${LATEST_STABLE} is available!"\n\
        else\n\
            echo "NOTE: Version ${VERSION} is the latest stable version"\n\
        fi\n\
    elif wget --spider -q "$UNSTABLE_URL" 2>/dev/null; then\n\
        DOWNLOAD_URL="$UNSTABLE_URL"\n\
        echo "Downloading Vintage Story Server version ${VERSION} from unstable..."\n\
        \n\
        # Check latest unstable version\n\
        UNSTABLE_JSON=$(wget -qO- https://api.vintagestory.at/unstable.json)\n\
        LATEST_UNSTABLE=$(echo "$UNSTABLE_JSON" | jq -r 'keys_unsorted[0]')\n\
        if [ "$VERSION" != "$LATEST_UNSTABLE" ]; then\n\
            echo "NOTE: You are running unstable version ${VERSION} but version ${LATEST_UNSTABLE} is available!"\n\
        else\n\
            echo "NOTE: Version ${VERSION} is the latest unstable version"\n\
        fi\n\
    else\n\
        echo "ERROR: Version ${VERSION} not found in either stable or unstable channels"\n\
        exit 1\n\
    fi\n\
    \n\
    wget -q "$DOWNLOAD_URL"\n\
    tar xzf vs_server_linux-x64_${VERSION}.tar.gz\n\
    rm vs_server_linux-x64_${VERSION}.tar.gz\n\
fi\n\
\n\
exec dotnet VintagestoryServer.dll --dataPath /srv/gameserver/data/vs\n\
' > /srv/gameserver/start.sh && chmod +x /srv/gameserver/start.sh

EXPOSE 42420

ENTRYPOINT ["/srv/gameserver/start.sh"]
