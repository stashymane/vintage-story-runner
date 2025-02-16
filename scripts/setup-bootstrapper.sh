case "$TARGETARCH" in
  "amd64")
    SRC="$BUILDPATH/linuxX64/releaseExecutable/bootstrapper.kexe"
    ;;
  "arm64")
    SRC="$BUILDPATH/linuxArm64/releaseExecutable/bootstrapper.kexe"
    ;;
  *)
    echo "Unsupported architecture"
    exit 1
    ;;
esac

cp "$SRC" /app/bootstrapper
