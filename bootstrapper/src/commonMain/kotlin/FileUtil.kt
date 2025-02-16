import data.Config
import data.PlatformInfo
import io.ktor.utils.io.*
import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString

fun Config.fetchCurrentVersion() =
    if (!SystemFileSystem.exists(versionPath)) null
    else SystemFileSystem.source(versionPath).buffered().use<Source, String>(Source::readString)


suspend fun setupGameFiles(version: String, platform: PlatformInfo) {
    SystemFileSystem.clearDirectory(config.gamePath, suppressExceptions = true)
    SystemFileSystem.createDirectories(config.gamePath)

    logger.info { "Downloading version $version (${platform.filesize})..." }
    val tempPath = downloadTarget(platform)
    logger.info { "Game files downloaded, unpacking..." }

    tempPath.untarTo(config.gamePath, true)
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

fun FileSystem.deleteRecursively(path: Path, mustExist: Boolean = true, suppressExceptions: Boolean = false) {
    val result = runCatching {
        if (metadataOrNull(path)?.isDirectory == true) {
            list(path).forEach {
                deleteRecursively(it, false, suppressExceptions)
            }
        }
        delete(path, mustExist)
    }
    if (!suppressExceptions) result.getOrThrow() else logger.warn { "Failed to delete $path: ${result.exceptionOrNull()?.message}" }
}

fun FileSystem.clearDirectory(path: Path, mustExist: Boolean = true, suppressExceptions: Boolean = false) {
    val result = runCatching {
        if (metadataOrNull(path)?.isDirectory == true) {
            list(path).forEach {
                deleteRecursively(it, false, suppressExceptions)
            }
        }
    }
}