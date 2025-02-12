variable "IMAGE_NAME" {
  default = "vintage-story-server"
}

variable "GITHUB_REF_NAME" {
  default = "dev"
}

target "vintage-story-server" {
  dockerfile = "Dockerfile"
  context = "."

  tags = [
    "${IMAGE_NAME}:${GITHUB_REF_NAME}"
  ]

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
