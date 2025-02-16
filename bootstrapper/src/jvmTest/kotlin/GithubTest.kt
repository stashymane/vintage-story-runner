import data.GithubRelease
import kotlinx.serialization.json.Json
import kotlin.test.Test

class GithubTest {
    @Test
    fun testRepository() {
        val releaseFile = readFile("/github-release.json")
        val json = Json { ignoreUnknownKeys = true }

        json.decodeFromString<List<GithubRelease>>(releaseFile)
    }
}
