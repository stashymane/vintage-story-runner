import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.io.Source
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.json.Json

val httpClient = HttpClient {
    install(Resources)
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

suspend fun HttpClient.saveFile(url: String, destination: Path) =
    get(url).body<Source>().use { source ->
        SystemFileSystem.sink(destination).use(source::transferTo)
    }
