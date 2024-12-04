# Vintage Story Docker
> Nicely contained, simple to setup container for running your own dedicated Vintage Story server!

> [!NOTE]
> This container supports both stable and release candidate (unstable) versions of Vintage Story. 

> [!IMPORTANT]
> This is a work in progress and is not yet ready for production use. Please check back later for updates!

## Features
- [x] Easy version configuration
- [x] Support for both stable and unstable (RC) versions
- [x] New version checks on startup
- [x] Data folder mounted as volume for easy management of saves, worlds, and server settings
- [ ] Better documentation
- [ ] Prebuilt Docker images for easy deployment
- [ ] Webhook support for notifications/logs
- [ ] Robust backup support
- [ ] Automated server restarts
- [ ] Easy to use web interface for server management (!)

## Setup

### Requirements
Docker - https://docs.docker.com/get-docker/

A PC to host the server from (can be the same one you play on). Official hardware requirements:
- **OS**: Windows or Linux
- **CPU**: 4 Threads recommended. Frequency: 1GHz base + 100MHz per player
- **RAM**: 1GB base + 300MB per player

### Basic setup

1. Create a directory for where you want to store the `compose.yaml` and server data. Let's call it `vintage-story-server` for this example.
2. Create a `compose.yaml` file in the `vintage-story-server` directory with the following contents:
    ```yaml
    services:
      vintage-story:
        build: 
          context: .
          dockerfile: Dockerfile
        container_name: vs-server
        restart: unless-stopped
        ports:
          - "42420:42420"
        volumes:
          - ./data:/srv/gameserver/data/vs
        environment:
          - TZ=Europe/London  # Set to your timezone
          - VERSION=1.20.0-rc.1   # Game version - works with release candidates (unstable) as well as regular releases (stable)
        stdin_open: true
        tty: true
    ```
3. Create a `Dockerfile` file in the `vintage-story-server` directory and paste the content from [here](Dockerfile) into it.
4. **IMPORTANT**: Create a `data` directory in the `vintage-story-server` directory. This is where the server data will be stored.
5. Run `docker-compose up -d` in the `vintage-story-server` directory to start the server.

> [!WARNING]
> **Do not run the server without the `data` directory already created!** 
> This is required as otherwise the server will create one for you with admin/root permissions!









