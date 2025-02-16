import io.ktor.client.*
import io.ktor.client.engine.curl.*

actual val httpClient: HttpClient = HttpClient(Curl) {
    engine {
        sslVerify = false //disabled due to old certs on base container
    }

    setup()
}
