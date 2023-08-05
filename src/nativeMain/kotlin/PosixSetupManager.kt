import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import okio.FileSystem
import okio.Path.Companion.toPath
import platform.posix.getenv

@OptIn(ExperimentalForeignApi::class)
private val CONFIG_FILE = "${getenv("HOME")?.toKString()}/.config/ticketchimp.json".toPath()

class PosixSetupManager {
    val configExists: Boolean
        get() = FileSystem.SYSTEM.exists(CONFIG_FILE)

    private fun askAuth(): AuthType? {
        println("Use basic or bearer auth? (basic/bearer):")
        return when (readlnOrNull()) {
            "basic" -> {
                val username = askUsername()
                val tokenKey = askTokenKey(true)
                if (username != null && tokenKey != null) AuthType.Basic(username, tokenKey) else null
            }
            "bearer" -> {
                val tokenKey = askTokenKey(false)
                if (tokenKey != null) AuthType.Bearer(tokenKey) else null
            }
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

    private fun askRepository(): String? {
        println("Path to repository:")
        return readlnOrNull()
    }

    fun startSetup() {
        val host = askHost() ?: error("Host cannot be null or empty.")
        val authType = askAuth() ?: error("Authorization cannot be null or empty.")
        val repositoryPath = askRepository() ?: error("Repository cannot be null or empty.")
        val config = Config(
            host = host,
            authType = authType,
            repositoryPath = repositoryPath,
        )
        FileSystem.SYSTEM.write(CONFIG_FILE) {
            writeUtf8(json.encodeToString(config))
            println("Config written to $CONFIG_FILE")
        }
    }

    fun readConfig(): Config {
        return FileSystem.SYSTEM.read(CONFIG_FILE) {
            val rawData = readUtf8()
            json.decodeFromString(rawData)
        }
    }
}

@Serializable
sealed class AuthType {
    abstract val tokenKey: String

    @Serializable
    class Basic(val username: String, override val tokenKey: String) : AuthType()

    @Serializable
    class Bearer(override val tokenKey: String) : AuthType()
}

@Serializable
data class Config(
    val host: String,
    val authType: AuthType,
    val repositoryPath: String,
)
