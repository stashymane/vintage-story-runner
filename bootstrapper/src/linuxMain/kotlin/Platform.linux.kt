import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import platform.posix.system

object LinuxNativePlatform : NativePlatform {
    override val targetName: String = "linuxserver"
}

actual fun NativePlatform.Companion.get(): NativePlatform = LinuxNativePlatform

actual fun Path.untarTo(destination: Path) {
    SystemFileSystem.createDirectories(destination)
    val result = system("tar -xzf $this -C $destination")
    if (result != 0) throw RuntimeException("`tar` returned non-zero result $result")
}
