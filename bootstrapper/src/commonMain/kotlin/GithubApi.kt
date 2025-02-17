import data.GitHub
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.datetime.*
import kotlin.time.Duration.Companion.seconds

suspend fun fetchArmRelease(version: String? = null): GitHub.Release {
    val response = httpClient.get("https://api.github.com/repos/${config.armRepo}/releases") {
        accept(ContentType.Application.Json)
        header("X-GitHub-Api-Version", "2022-11-28")
    }

    if (response.status.value == 403 || response.status.value == 429) {
        val reset =
            response.headers["x-ratelimit-reset"]?.let { Instant.fromEpochSeconds(it.toLong()) }?.plus(1.seconds)
                ?: throw RuntimeException("GitHub API rate limit reached, please try again later")
        val duration = reset - Clock.System.now()
        logger.warn {
            "GitHub API rate limit reached, waiting for $duration (${
                reset.toLocalDateTime(TimeZone.currentSystemDefault()).format(LocalDateTime.Formats.ISO)
            })..."
        }
        delay(duration)
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
