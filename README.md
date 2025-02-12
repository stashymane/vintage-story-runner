# Vintage Story Dedicated Server (fork)
> This fork introduces `arm64` target support and simplifies configuration.

### Requirements
- Docker - https://docs.docker.com/get-docker/

- A PC to host the server from (can be the same one you play on). Official hardware requirements:
  - **OS**: Windows or Linux
  - **CPU**: 4 Threads recommended. Frequency: 1GHz base + 100MHz per player
  - **RAM**: 1GB base + 300MB per player

## Usage / Setup

### Quick Start

`docker-compose.yml`
```yaml
services:
  vintage-story-server:
    image: vintage-story-server:latest
    container_name: vs-server

    environment:
      VERSION: "1.20.3" # any game version

    volumes:
      - "./data:/data" # [host directory]:[container directory]

    ports: ["42420:42420"] # [host port]:[container port]
    stdin_open: true
    tty: true
```

### Development

* `docker buildx bake && docker compose up --no-build` to rebuild & run;
* `docker compose down` to take down;
* let CI take care of the rest.

`bake` will only build for your local platform if configuration files are not set explicitly.

Actions ignores the `docker-bake.override.hcl` file.
