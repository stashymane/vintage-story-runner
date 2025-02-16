object LinuxNativePlatform : NativePlatform {
    override val targetName: String = "linuxserver"
    override fun run() = linuxRun()
}

actual fun NativePlatform.Companion.get(): NativePlatform = LinuxNativePlatform
