package data

import kotlinx.io.files.Path
import kotlinx.io.files.SystemTemporaryDirectory
import kotlin.jvm.JvmInline

data class Config(
    val stableUrl: String = readEnv("STABLE_URL") ?: "https://api.vintagestory.at/stable.json",
    val unstableUrl: String = readEnv("UNSTABLE_URL") ?: "https://api.vintagestory.at/unstable.json",
    val branch: Branch = Branch(readEnv("BRANCH") ?: "stable"),
    val version: String? = readEnv("VERSION"),
    val armVersion: String? = readEnv("ARM_VERSION"),
    val armRepo: String = readEnv("ARM_REPO") ?: "anegostudios/VintagestoryServerArm64",
    val gamePath: Path = Path(readEnv("GAME_PATH") ?: "/game"),
    val dataPath: Path = Path(readEnv("DATA_PATH") ?: "/data"),
    val tempPath: Path = readEnv("TEMP_PATH")?.let { Path(it) } ?: SystemTemporaryDirectory
) {
    val url: String = when (branch) {
        Branch.Stable -> stableUrl
        Branch.Unstable -> unstableUrl
        else -> throw AssertionError("Unknown branch: $branch")
    }

    val versionPath = Path(gamePath, "VERSION")

    @JvmInline
    value class Branch(val name: String) {
        companion object {
            val Stable = Branch("stable")
            val Unstable = Branch("unstable")
        }
    }
}

expect fun readEnv(name: String): String?
