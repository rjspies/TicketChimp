import io.ktor.client.HttpClient
import io.ktor.client.engine.curl.Curl
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.bearerAuth
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.cinterop.toKString
import kotlinx.serialization.json.Json
import platform.posix.getenv

fun createKtorHttpClient(
    tokenKey: String,
    host: String,
) = HttpClient(Curl) {
    install(Resources)

    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }

    install(ContentNegotiation) {
        val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }
        json(json)
    }

    defaultRequest {
        bearerAuth(getenv(tokenKey)?.toKString() ?: throw Exception("Env"))
        url {
            protocol = URLProtocol.HTTPS
            this.host = host
            path("rest/api/latest/")
        }
    }
}
