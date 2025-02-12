target "docker-metadata-action" {}

target "vintage-story-server" {
  inherits = ["docker-metadata-action"]

  dockerfile = "Dockerfile"
  context = "."

  platforms = [
    "linux/amd64",
    "linux/arm64"
  ]

  cache-from = ["type=gha"]
  cache-to = ["type=gha"]
}

group "default" {
  targets = ["vintage-story-server"]
}
