import data.Versions
import kotlinx.serialization.json.Json
import kotlin.test.Test

class VSApiTest {
    @Test
    fun testApi() {
        val stableFile = readFile("/stable.json")
        val json = Json { ignoreUnknownKeys = true }

        json.decodeFromString<Versions>(stableFile)
    }
}
