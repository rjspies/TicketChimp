import io.ktor.resources.Resource
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun main(arguments: Array<String>) {
    val parser = ArgParser("TicketChimp")
    val setup by parser.option(ArgType.Boolean, shortName = "s", description = "Starts the setup", fullName = "setup")
    parser.parse(arguments)

    if (setup == true) {
        val setupManager = PosixSetupManager()
        setupManager.startSetup()
    } else {
        println("Doing nothing then")
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
