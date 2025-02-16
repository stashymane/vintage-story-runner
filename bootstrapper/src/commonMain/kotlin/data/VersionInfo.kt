package data

import kotlinx.serialization.Serializable

@Serializable
data class VersionInfo(
    val latestVersion: String,
    val version: String,
    val target: PlatformInfo
)