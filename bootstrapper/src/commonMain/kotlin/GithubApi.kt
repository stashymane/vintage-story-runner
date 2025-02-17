import data.GitHub
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.datetime.*

suspend fun fetchArmRelease(version: String? = null): GitHub.Release {
    val response = httpClient.get("https://api.github.com/repos/${config.armRepo}/releases") {
        accept(ContentType.Application.Json)
        header("X-GitHub-Api-Version", "2022-11-28")
    }

    if (response.status.value == 403 || response.status.value == 429) {
        val reset = response.headers["x-ratelimit-reset"]?.let { Instant.fromEpochSeconds(it.toLong()) }
            ?: throw RuntimeException("GitHub API rate limit reached, please try again later")
        logger.warn {
            "GitHub API rate limit reached, waiting until ${
                reset.toLocalDateTime(TimeZone.currentSystemDefault()).format(LocalDateTime.Formats.ISO)
            }..."
        }
        delay(reset - Clock.System.now())
        return fetchArmRelease(version)
    }

    if (!response.status.isSuccess()) {
        logger.error { "Failed to fetch ARM release from GitHub API." }
        val error = response.body<GitHub.ApiError>()
        logger.error { "Response: ${error.message}" }
        throw RuntimeException("Failed to fetch ARM release")
    }

    val releases = response.body<List<GitHub.Release>>()

    return releases.first { version == null || it.name == version }
}
