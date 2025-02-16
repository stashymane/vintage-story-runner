import kotlinx.io.files.Path

interface NativePlatform {
    companion object {}

    val targetName: String
    suspend fun setup(version: String) {
        logger.info { "Platform setup not required, skipping..." }
    }
}

expect fun NativePlatform.Companion.get(): NativePlatform

expect fun Path.untarTo(destination: Path)
