FROM alpine:latest AS builder
ARG TARGETARCH
ENV BUILDPATH=/bootstrapper

COPY ./bootstrapper/build/bin $BUILDPATH
COPY ./scripts/setup-bootstrapper.sh /setup.sh
RUN mkdir /game && chmod +x /setup.sh && /setup.sh

FROM mcr.microsoft.com/dotnet/runtime:7.0
ARG TARGETARCH

RUN apt-get update && apt-get install -y \
    wget jq \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir /game
VOLUME /data

WORKDIR /game

COPY scripts/prepare.sh /game
COPY --from=builder /game/bootstrapper /game/bootstrapper

RUN chmod +x /game/*.sh

EXPOSE 42420

ENTRYPOINT ["/game/prepare.sh"]
