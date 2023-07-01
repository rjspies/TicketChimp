import kotlinx.cinterop.toKString
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path.Companion.toPath
import platform.posix.getenv

class PosixSetupManager {
    private fun askAuth(): AuthType? {
        println("Use basic or bearer auth? (basic/bearer):")
        return when (readlnOrNull()) {
            "basic" -> AuthType.Basic
            "bearer" -> AuthType.Bearer
            else -> null
        }
    }

    private fun askHost(): String? {
        println("Jira host? (e. g. \"company.atlassian.net\"):")
        return readlnOrNull()
    }

    private fun askUsername(): String? {
        println("Username?:")
        return readlnOrNull()
    }

    private fun askTokenKey(basicAuth: Boolean): String? {
        println("Token key stored in your environment variables? (not the actual token value):")
        if (basicAuth) println("Basic auth also uses the token instead of a password.")
        return readlnOrNull()
    }

    fun startSetup() {
        val host = askHost()
        val authType = askAuth()
        val basicAuth = authType == AuthType.Basic
        val username = if (basicAuth) askUsername() else null
        val tokenKey = askTokenKey(basicAuth)
        val config = Config(
            host = host,
            authType = authType,
            username = username,
            tokenKey = tokenKey,
        )
        println("${getenv("HOME")?.toKString()}/.config/ticketchimp.json")
        FileSystem.SYSTEM.write("${getenv("HOME")?.toKString()}/.config/ticketchimp.json".toPath()) {
            val json = Json { prettyPrint = true }
            writeUtf8(json.encodeToString(config))
        }
    }
}

enum class AuthType {
    Basic,
    Bearer,
}

@Serializable
data class Config(
    val host: String?,
    val authType: AuthType?,
    val username: String?,
    val tokenKey: String?,
)
