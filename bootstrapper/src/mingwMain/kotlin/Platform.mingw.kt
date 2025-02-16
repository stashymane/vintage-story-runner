import kotlinx.io.files.Path

object WindowsNativePlatform : NativePlatform {
    override val targetName: String = "windowsserver"
    override fun run() {
        TODO("Not yet implemented")
    }
}

actual fun NativePlatform.Companion.get(): NativePlatform = WindowsNativePlatform

actual fun Path.untarTo(destination: Path, skipIfExists: Boolean) {
    TODO("Not yet implemented")
}
