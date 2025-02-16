FROM --platform=$BUILDPLATFORM alpine:latest AS builder
ARG TARGETARCH
ENV BUILDPATH=/bootstrapper

COPY --chmod=777 ./bootstrapper/build/bin/ $BUILDPATH/
COPY --chmod=777 ./scripts/setup-bootstrapper.sh /setup.sh
RUN mkdir /app; \
    /setup.sh

FROM mcr.microsoft.com/dotnet/runtime:7.0
ARG TARGETARCH

VOLUME /data
EXPOSE 42420

RUN mkdir /app /game;\
    chmod 777 /game
COPY --from=builder --chmod=777 /app/bootstrapper /app/bootstrapper
WORKDIR /game

CMD ["/app/bootstrapper"]
