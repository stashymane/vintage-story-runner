import kotlinx.io.files.Path

object WindowsNativePlatform : NativePlatform {
    override val targetName: String = "windowsserver"
}

actual fun NativePlatform.Companion.get(): NativePlatform = WindowsNativePlatform

actual fun Path.untarTo(destination: Path) {
    TODO("Not yet implemented")
}
