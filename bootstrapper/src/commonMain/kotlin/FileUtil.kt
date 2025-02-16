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
    if (!suppressExceptions) result.getOrThrow() else result.exceptionOrNull()?.let { logger.warn(it) { "Failed to delete $path" } }
}

fun FileSystem.clearDirectory(path: Path, mustExist: Boolean = true, suppressExceptions: Boolean = false) {
    if (!exists(path)) return

    println(list(path).joinToString(", ", transform = Path::toString))
    val result = runCatching {
        list(path).forEach {
            deleteRecursively(it, false, suppressExceptions)
        }
    }
    if (!suppressExceptions) result.getOrThrow() else result.exceptionOrNull()?.let { logger.warn(it) { "Failed to delete $path" } }
}
