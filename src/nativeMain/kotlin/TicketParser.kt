import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.resources.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class TicketParser(private val httpClient: HttpClient) {
    suspend fun parseTicket(ticketKey: String): Issue {
        return httpClient.get(Ticket(ticketKey)).body<Issue>()
    }
}

@Resource("/issue/{key}")
data class Ticket(val key: String)

@Serializable
data class Issue(val key: String, val fields: Fields)

@Serializable
data class Fields(@SerialName("issuetype") val issueType: IssueType, val summary: String)

@Serializable
data class IssueType(val name: String)
