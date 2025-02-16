FROM --platform=$BUILDPLATFORM alpine:latest AS builder
ARG TARGETARCH
ENV BUILDPATH=/bootstrapper

COPY ./bootstrapper/build/bin/ $BUILDPATH/
COPY ./scripts/setup-bootstrapper.sh /setup.sh
RUN mkdir /game; \
    chmod +x /setup.sh; \
    /setup.sh

FROM mcr.microsoft.com/dotnet/runtime:7.0
ARG TARGETARCH

VOLUME /data
EXPOSE 42420

RUN mkdir /game;\
    chmod 777 /game
WORKDIR /game

COPY --from=builder /bootstrapper /bootstrapper
RUN chmod +x /bootstrapper

CMD ["/bootstrapper"]
