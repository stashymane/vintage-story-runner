import data.GithubRelease
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun fetchArmRelease(version: String? = null): GithubRelease {
    val releases = httpClient.get("https://api.github.com/repos/${config.armRepo}/releases") {
        accept(ContentType.Application.Json)
        header("X-GitHub-Api-Version", "2022-11-28")
    }.body<List<GithubRelease>>()

    return releases.first { version == null || it.name == version }
}
