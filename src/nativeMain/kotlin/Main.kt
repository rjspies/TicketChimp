import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.resources.Resource
import io.ktor.utils.io.core.use
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun main(arguments: Array<String>) {
    val ticketKey = arguments.firstOrNull() ?: throw IllegalArgumentException("The first argument must be the key of the Jira issue")

    val httpClient = createKtorHttpClient(
        tokenKey = "QM_JIRA_TICKET_PRAKTIKANT_TOKEN",
        host = "jira.quartett-mobile.de",
    )

    httpClient.use {
        runBlocking {
            val issue: Issue = it.get(Ticket(ticketKey)).body()
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
