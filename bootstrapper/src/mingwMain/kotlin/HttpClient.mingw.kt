import io.ktor.client.*
import io.ktor.client.engine.winhttp.*

actual val httpClient: HttpClient = HttpClient(WinHttp) {
    setup()
}
