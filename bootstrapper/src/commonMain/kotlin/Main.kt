import data.Config
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking


val logger = KotlinLogging.logger("Bootstrapper")
val config = Config()

fun main() = runBlocking {
    val installedVersion = config.fetchCurrentVersion()
    val (latestVersion, desiredVersion, platform) = fetchVersions(config.url, config.version)

    when {
        installedVersion == null || installedVersion != desiredVersion -> setupGameFiles(desiredVersion, platform)
        latestVersion != desiredVersion -> logger.info { "A newer version is available ($latestVersion)" }
        installedVersion == desiredVersion -> logger.info { "Files are up-to-date." }
    }

    NativePlatform.get().run()
}
