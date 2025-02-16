import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

object LinuxArmNativePlatform : NativePlatform {
    override val targetName: String = "linuxserver"
    private val overriddenPaths = listOf<String>(
        "VintagestoryServer",
        "VintagestoryServer.deps.json",
        "VintagestoryServer.dll",
        "VintagestoryServer.pdb",
        "VintagestoryServer.runtimeconfig.json",
        "Lib"
    )

    override suspend fun setup(version: String) {
        logger.info { "Setting up arm64 libraries..." }

        val release = fetchArmRelease(config.armVersion)

        val asset = release.assets.first()
        val tempFilePath = Path(config.tempPath, asset.name)

        httpClient.saveFile(asset.downloadUrl, tempFilePath)

        overriddenPaths.map { Path(config.gamePath, it) }
            .filter(SystemFileSystem::exists)
            .forEach {
                if (SystemFileSystem.isDirectory(it)) SystemFileSystem.deleteRecursively(it)
                else SystemFileSystem.delete(it)
            }

        tempFilePath.untarTo(config.gamePath)

        SystemFileSystem.delete(tempFilePath)
    }

    override fun run() = linuxRun()
}

actual fun NativePlatform.Companion.get(): NativePlatform = LinuxArmNativePlatform
