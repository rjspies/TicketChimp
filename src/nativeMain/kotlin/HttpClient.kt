import io.ktor.client.HttpClient
import io.ktor.client.engine.curl.Curl
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.basicAuth
import io.ktor.client.request.bearerAuth
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

@OptIn(ExperimentalForeignApi::class)
fun createKtorHttpClient(
    host: String,
    authType: AuthType,
) = HttpClient(Curl) {
    install(Resources)

    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }

    install(ContentNegotiation) {
        json(json)
    }

    defaultRequest {
        val token = getenv(authType.tokenKey)?.toKString() ?: throw IllegalArgumentException("Could not get token from environment variable \"${authType.tokenKey}\".")
        if (authType is AuthType.Basic) basicAuth(authType.username, token) else bearerAuth(token)
        url {
            protocol = URLProtocol.HTTPS
            this.host = host
            path("rest/api/latest/")
        }
    }
}
