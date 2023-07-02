import io.ktor.resources.Resource
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.system.exitProcess

fun main(arguments: Array<String>) {
    val parser = ArgParser("TicketChimp")
    val setup by parser.option(ArgType.Boolean, shortName = "s", description = "Starts the setup", fullName = "setup")
    val ticket by parser.argument(ArgType.String, description = "Jira ticket number (XX-XXX)", fullName = "ticket").optional()
    val setupManager = PosixSetupManager()

    parser.parse(arguments)

    when {
        !setupManager.configExists && setup != true -> {
            println("No config file found. Run the setup using \"-s\" first.")
            exitProcess(0)
        }

        setup == true -> setupManager.startSetup()
        ticket != null -> println("Parsing ticket...")
        setupManager.configExists -> {
            println("Provide a ticket number for parsing.")
            exitProcess(0)
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
