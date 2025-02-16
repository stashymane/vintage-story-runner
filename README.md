# Vintage Story Runner
Downloads Vintage Story & runs it in a container. Supports both `x64` and `arm64` platforms.

### Requirements
- A PC to host the server from (can be the same one you play on). Official hardware requirements:
  - **OS**: Windows or Linux
  - **CPU**: 4 Threads recommended. Frequency: 1GHz base + 100MHz per player
  - **RAM**: 1GB base + 300MB per player

## Usage

`docker-compose.yml`
```yaml
services:
  vintage-story-server:
    image: stashymane/vintage-story-runner:latest
    user: 1000:1000 # set user/group to your local uid/gid for file permissions
    container_name: vs-server

    environment:
      VERSION: "1.20.4" # any game version

    volumes:
      - "./data:/data" # [host directory]:[container directory]

    ports: ["42420:42420"] # [host port]:[container port]
    stdin_open: true
    tty: true
```

### Configuration

You can configure this container with environment variables. All of these are optional.

| Variable      |  Default   | Description                                                       |
|---------------|:----------:|-------------------------------------------------------------------|
| `VERSION`     |  [latest]  | The game version to download                                      |
| `BRANCH`      |  `stable`  | Which branch of the game to download                              |
| `ARM_VERSION` |  [latest]  | the version of the ARM server release to use on `arm64` platforms |
| `GAME_PATH`   |  `/game`   | the path the game will be installed to and run from               |
| `DATA_PATH`   |  `/data`   | the path your game will save data to                              |

The following options are not recommended to set, but are available if necessary.
* `STABLE_URL`/`UNSTABLE_URL` - links to Vintage Story API (`stable.json`/`unstable.json`)
* `ARM_REPO` - the GitHub repository to pull ARM versions from (`username/repo`)
* `TEMP_PATH` - mostly used for testing, sets the path for temporary files

### Development
1. `./bootstrapper/gradlew build -p ./bootstrapper` - builds the bootstrapper (required)
2. `docker buildx bake` - bakes the container image
3. `docker compose up --no-build` - run the built image

This is all done by CI as well.

Note: `docker buildx bake` will only build for your local platform if configuration files are not set explicitly.
The GitHub action ignores the `docker-bake.override.hcl` file.
