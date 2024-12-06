# Vintage Story Dedicated Server
> Nice and simple to setup Docker container for running your own dedicated Vintage Story server!

> [!NOTE]
> This container supports both stable and release candidate (unstable) versions of Vintage Story. 

## Features
- [x] Easy version configuration
- [x] Support for both stable and unstable (RC) versions
- [x] New version checks on startup
- [x] Data folder mounted as volume for easy management of saves, worlds, and server settings
- [x] Prebuilt Docker images for easy deployment
- [ ] Better documentation
- [ ] Webhook support for notifications/logs
- [ ] Robust backup support
- [ ] Automated server restarts
- [ ] Easy to use web interface for server management (!)



### Requirements
- Docker - https://docs.docker.com/get-docker/

- A PC to host the server from (can be the same one you play on). Official hardware requirements:
  - **OS**: Windows or Linux
  - **CPU**: 4 Threads recommended. Frequency: 1GHz base + 100MHz per player
  - **RAM**: 1GB base + 300MB per player

## Usage / Setup

### Quick Start

Create a directory for where you want to store the `compose.yaml` and server data. Let's call it `vintage-story-server` for this example. Make sure to create a `data` directory in the `vintage-story-server` directory first, and then create a `compose.yaml` file in the `vintage-story-server` directory with the following contents:
```yaml
services:
  vintage-story:
    image: quartzar/vintage-story-server:latest
    container_name: vs-server
    restart: unless-stopped
    ports:
      - "42420:42420"  # HOST:CONTAINER - Change the HOST port if you want to use a different port
    volumes:
      - ./data:/srv/gameserver/data/vs
    environment:
      - TZ=Europe/London  # Set to your timezone (optional)
      - VERSION=1.20.0-rc.1   # Game version - works with release candidates (unstable) as well as regular releases (stable)
    stdin_open: true
    tty: true
```
**Make sure to set the `VERSION` of the game you wish to use** - it will work with both stable and unstable versions. Visit the [Vintage Story website](https://www.vintagestory.at/) to find the latest version number.

Run `docker compose up -d` in the `vintage-story-server` directory to start the server, and that's it! 

You can now connect to your server using the IP of the host machine and the port you specified in the `compose.yaml` file (default is `42420`). 

To stop the container, just run `docker compose down` in the `vintage-story-server` directory.

> [!WARNING]
> **Do not run the server without the `data` directory already created!** 
> This is required as otherwise the server will create one for you with admin/root permissions!

#### Extra Tips

Often it's easier to generate a world in singleplayer first and copy it over - you can do this after starting the server at least once, stopping it (`docker compose down` or `CTRL+C` if you didn't start it detached (`-d`)), and copying the world save into `./data/Saves` in the `vintage-story-server` directory.



If you want to edit the server settings, they can be found in the `data` directory you created, once the server has started, along with world saves, backups, mods, logs, and more. Just make sure to restart the server when making changes to the `serverconfig.json`/`servermagicnumbers.json` file.

Optionally, you could also create a Docker volume instead of using the local bind mount to `./data` for the server data. This is useful if you want to manage the server data separately from the server container. To do this, create a volume with `docker volume create vs-data` and then replace the `volumes` section in the `compose.yaml` file with `- vs-data:/srv/gameserver/data/vs`. Technically, you could remove the volume mount all together, but this is not recommended as it will make it harder to manage the server data (you'd need to enter the container/use `docker cp` commands).





### Development 

Create a directory for where you want to store the `compose.yaml` and server data. Let's call it `vintage-story-server` for this example.

Create a `compose.yaml` file in the `vintage-story-server` directory with the following contents:
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

Create a `Dockerfile` file in the `vintage-story-server` directory and paste the content from [here](Dockerfile) into it.

**IMPORTANT**: Create a `data` directory in the `vintage-story-server` directory. This is where the server data will be stored.

Run `docker-compose up -d` in the `vintage-story-server` directory to start the server.











