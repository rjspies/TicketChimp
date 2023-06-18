import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.resources.Resource
import io.ktor.utils.io.core.use
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun main(arguments: Array<String>) {
    val firstArgument = arguments.firstOrNull() ?: throw IllegalArgumentException("The first argument must be the key of the Jira issue")

    val setupManager = PosixSetupManager()
    if (firstArgument.startsWith("-")) {
        setupManager.launchSetup()
    } else if (!setupManager.configExists) {
        throw IllegalArgumentException("You first need to go through the setup using the \"-setup\" option")
    }
    val config = setupManager.readConfig()
    setupManager.close()

    val httpClient = createKtorHttpClient(
        tokenKey = config.tokenKey ?: throw Exception("No token key provided"),
        host = "host",
    )

    httpClient.use {
        runBlocking {
            val issue: Issue = it.get(Ticket(firstArgument)).body()
            println("key = ${issue.key}")
        }
    }
}

@Resource("/issue/{key}")
class Ticket(val key: String)

@Serializable
data class Issue(val key: String, val fields: Fields)

@Serializable
data class Fields(@SerialName("issuetype") val issueType: IssueType, val summary: String)

@Serializable
data class IssueType(val name: String)
