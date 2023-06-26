import kotlinx.serialization.Serializable

class PosixSetupManager {
    private fun askAuth(): AuthType {
        println("Use basic or bearer auth? (basic/bearer):")
        return when (readlnOrNull()) {
            "basic" -> AuthType.Basic
            "bearer" -> AuthType.Bearer
            else -> AuthType.Undefined
        }
    }

    fun startSetup() {
        val authType = askAuth()
        println(authType)
    }
}

enum class AuthType {
    Basic,
    Bearer,
    Undefined,
}

@Serializable
data class Config(
    val bearerAuth: Boolean,
    val tokenKey: String?,
)
