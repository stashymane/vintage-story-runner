package data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object GitHub {
    @Serializable
    data class Release(
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

    @Serializable
    data class ApiError(
        val message: String
    )
}
