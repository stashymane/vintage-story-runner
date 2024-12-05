#!/bin/bash

if [ -z "$VERSION" ]; then
    echo "ERROR: VERSION environment variable must be set"
    exit 1
fi

STABLE_URL="${STABLE_URL}${VERSION}.tar.gz"
UNSTABLE_URL="${UNSTABLE_URL}${VERSION}.tar.gz"

# Try stable URL first
if wget --spider -q "$STABLE_URL" 2>/dev/null; then
    DOWNLOAD_URL="$STABLE_URL"
    echo "Downloading Vintage Story Server version ${VERSION} from stable..."
elif wget --spider -q "$UNSTABLE_URL" 2>/dev/null; then
    DOWNLOAD_URL="$UNSTABLE_URL"
    echo "Downloading Vintage Story Server version ${VERSION} from unstable..."
else
    echo "ERROR: Version ${VERSION} not found in either stable or unstable channels"
    exit 1
fi

wget -q "$DOWNLOAD_URL"
tar xzf vs_server_linux-x64_${VERSION}.tar.gz
rm vs_server_linux-x64_${VERSION}.tar.gz

echo "$VERSION" > current_version
