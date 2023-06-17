import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.resources.Resource
import io.ktor.utils.io.core.use
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

fun main() {
    val httpClient = createKtorHttpClient(
        tokenKey = "QM_JIRA_TICKET_PRAKTIKANT_TOKEN",
        host = "jira.quartett-mobile.de",
    )

    httpClient.use {
        runBlocking {
            val issue: Issue = it.get(Ticket("ONE-23928")).body()
            println("key = ${issue.key}")
        }
    }
}

@Resource("/issue/{key}")
class Ticket(val key: String)

@Serializable
data class Issue(val key: String)
