package data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubRelease(
    val url: String,
    val id: Int,
    val name: String,
    val body: String,
    val assets: List<Asset>
) {
    @Serializable
    data class Asset(
        val url: String,
        @SerialName("browser_download_url") val downloadUrl: String,
        val id: Int,
        val name: String,
    )
}
