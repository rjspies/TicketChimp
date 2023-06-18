import io.ktor.utils.io.core.Closeable
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path.Companion.toPath
import platform.posix.F_OK
import platform.posix.O_CREAT
import platform.posix.O_RDWR
import platform.posix.O_TRUNC
import platform.posix.S_IRUSR
import platform.posix.S_IRWXU
import platform.posix.S_IWUSR
import platform.posix.access
import platform.posix.close
import platform.posix.getenv
import platform.posix.mkdir
import platform.posix.open
import platform.posix.write

private const val RELATIVE_CONFIG_PATH = ".config/ticketchimp.json"

class PosixSetupManager : Closeable {
    private val fileDescriptor by lazy {
        if (!configExists) {
            mkdir(configFile().substringBeforeLast("/"), S_IRWXU)
        }
        val fileDescriptor = open(configFile(), O_RDWR or O_CREAT or O_TRUNC, S_IRUSR or S_IWUSR)
        require(fileDescriptor != -1) {
            "File descriptor is -1"
        } // TODO throw specific exception
        fileDescriptor
    }

    private val json by lazy { Json { prettyPrint = true } }

    val configExists: Boolean
        get() = fileExists(configFile())

    private fun configFile(): String {
        val homeDirectory = getenv("HOME")?.toKString()?.trimEnd('/') ?: throw IllegalArgumentException("Home environment variable not found") // TODO throw specific exception
        return "$homeDirectory/$RELATIVE_CONFIG_PATH"
    }

    private fun fileExists(path: String): Boolean {
        val result = access(path, F_OK)
        return result == 0
    }

    private fun writeToConfigFile(bytesToWrite: ByteArray): Long {
        return write(fileDescriptor, bytesToWrite.refTo(0), bytesToWrite.size.toULong())
    }

    private fun ask(question: String): Boolean {
        println(question)
        val rawAnswer = readlnOrNull() ?: throw IllegalArgumentException("Input cannot be null") // TODO throw specific exception
        return "y" == rawAnswer
    }

    private fun askString(question: String): String {
        println(question)
        return readlnOrNull() ?: throw IllegalArgumentException("Input cannot be null") // TODO throw specific exception
    }

    fun launchSetup() {
        println("Welcome to the setup!")
        val bearerAuth = ask("Use bearer auth? (y/n)")
        val tokenKey = if (bearerAuth) askString("The key of your Jira's token environment variable:") else null
        val config = Config(bearerAuth, tokenKey)
        val jsonString = json.encodeToString(config)
        writeToConfigFile(jsonString.encodeToByteArray())
    }

    fun readConfig(): Config {
        val path = configFile().toPath()
        val content = FileSystem.SYSTEM.read(path) { readUtf8() }
        return json.decodeFromString(content)
    }

    override fun close() {
        close(fileDescriptor)
    }
}

@Serializable
data class Config(
    val bearerAuth: Boolean,
    val tokenKey: String?,
)
