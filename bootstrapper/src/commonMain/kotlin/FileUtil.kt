import data.Config
import data.PlatformInfo
import io.ktor.utils.io.*
import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString

fun Config.fetchCurrentVersion() =
    if (!SystemFileSystem.exists(versionPath)) null
    else SystemFileSystem.source(versionPath).buffered().use<Source, String>(Source::readString)


suspend fun setupGameFiles(version: String, platform: PlatformInfo) {
    logger.info { "Downloading version $version (${platform.filesize})..." }
    val tempPath = downloadTarget(platform)
    logger.info { "Game files downloaded, unpacking..." }

    if (SystemFileSystem.exists(config.gamePath)) SystemFileSystem.delete(config.gamePath)
    SystemFileSystem.createDirectories(config.gamePath)

    tempPath.untarTo(config.gamePath)
    SystemFileSystem.delete(tempPath)

    SystemFileSystem.sink(config.versionPath).use { sink ->
        sink.asByteWriteChannel().apply {
            writeString(version)
            flush()
        }
    }
    logger.info { "Unpacked into ${config.gamePath}" }

    NativePlatform.get().setup(version)
}
