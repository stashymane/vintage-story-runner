object LinuxNativePlatform : NativePlatform {
    override val targetName: String = "linuxserver"
}

actual fun NativePlatform.Companion.get(): NativePlatform = LinuxNativePlatform
