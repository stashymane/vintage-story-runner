package data

import kotlinx.serialization.Serializable

typealias Versions = Map<String, Platforms>
typealias Platforms = Map<String, PlatformInfo>

@Serializable
data class PlatformInfo(
    val filename: String,
    val filesize: String,
    val md5: String,
    val urls: Urls,
    val latest: Int? = null
) {
    @Serializable
    data class Urls(
        val cdn: String? = null,
        val local: String? = null
    )
}
