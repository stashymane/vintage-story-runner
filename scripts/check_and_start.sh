#!/bin/bash

if [ -z "$VERSION" ]; then
    echo "ERROR: VERSION environment variable must be set"
    exit 1
fi

# Check if we need to download/update server files
if [ ! -f VintagestoryServer.dll ] || [ ! -f current_version ] || [ "$(cat current_version)" != "$VERSION" ]; then
    ./download_server.sh
fi

# Check latest versions
STABLE_JSON=$(wget -qO- https://api.vintagestory.at/stable.json)
LATEST_STABLE=$(echo "$STABLE_JSON" | jq -r 'keys_unsorted[0]')
UNSTABLE_JSON=$(wget -qO- https://api.vintagestory.at/unstable.json)
LATEST_UNSTABLE=$(echo "$UNSTABLE_JSON" | jq -r 'keys_unsorted[0]')

# Check if stable version
if wget --spider -q "${STABLE_URL}${VERSION}.tar.gz" 2>/dev/null; then
    if [ "$VERSION" != "$LATEST_STABLE" ]; then
        echo "NOTE: You are running stable version ${VERSION} but version ${LATEST_STABLE} is available!"
    else
        echo "NOTE: Current version ${VERSION} is the latest stable version"
    fi
else
    if [ "$VERSION" != "$LATEST_UNSTABLE" ]; then
        echo "NOTE: You are running unstable version ${VERSION} but version ${LATEST_UNSTABLE} is available!"
    else
        echo "NOTE: Current version ${VERSION} is the latest unstable version"
    fi
fi

exec dotnet VintagestoryServer.dll --dataPath /srv/gameserver/data/vs
