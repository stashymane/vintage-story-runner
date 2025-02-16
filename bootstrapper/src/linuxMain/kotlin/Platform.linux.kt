import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cstr
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCValues
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import platform.posix.execvp
import platform.posix.exit
import platform.posix.perror
import platform.posix.system

actual fun Path.untarTo(destination: Path, skipIfExists: Boolean) {
    SystemFileSystem.createDirectories(destination)
    val result = system("tar -xzf $this -C $destination ${if (skipIfExists) "--skip-old-files" else ""}")
    if (result != 0) throw RuntimeException("`tar` returned non-zero result $result")
}

@OptIn(ExperimentalForeignApi::class)
fun linuxRun() = memScoped {
    val args =
        arrayOf("dotnet", "${config.gamePath}/VintagestoryServer.dll", "--dataPath", config.dataPath.toString(), null)
    execvp(args[0], args.map { it?.cstr?.ptr }.toCValues())

    perror("Failed to start Vintage Story server")
    exit(1)
}
