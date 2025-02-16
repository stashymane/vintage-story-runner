import data.PlatformInfo
import data.Versions
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.Serializable

@Serializable
data class VersionInfo(
    val latestVersion: String,
    val version: String,
    val target: PlatformInfo
)

suspend fun fetchVersions(
    url: String,
    version: String? = null,
    platform: String = NativePlatform.get().targetName
): VersionInfo {
    val versions = httpClient.get(url).body<Versions>()
        .mapValues { (_, platforms) -> platforms[platform] }

    val (latestVersion, latestTarget) = versions.entries
        .firstOrNull { (_, target) -> target?.latest == 1 }
        ?: throw NullPointerException("Failed to find latest version on $url")

    if (latestTarget == null) throw RuntimeException("Version $version does not have a $platform target")

    return when (version) {
        null -> VersionInfo(latestVersion, latestVersion, latestTarget)
        else -> VersionInfo(
            latestVersion,
            version,
            versions[version] ?: throw NullPointerException("Failed to find version $version on $url")
        )
    }
}

suspend fun downloadTarget(target: PlatformInfo): Path {
    val url = target.urls.cdn ?: target.urls.local ?: throw NullPointerException("Failed to find download URL")

    val tempPath = Path(config.tempPath, target.filename)
    if (SystemFileSystem.exists(tempPath)) SystemFileSystem.delete(tempPath)

    SystemFileSystem.createDirectories(config.tempPath)

    httpClient.saveFile(url, tempPath)

    return tempPath
}
