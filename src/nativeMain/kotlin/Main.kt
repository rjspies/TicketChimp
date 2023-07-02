import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.optional
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
        ticket != null -> {
            val config = setupManager.readConfig()
            val host = config.host
            val authType = config.authType
            val httpClient = createKtorHttpClient(
                host = host,
                authType = authType,
            )
            val ticketParser = TicketParser(httpClient)
            ticketParser.parseTicket(ticket!!)
        }

        setupManager.configExists -> {
            println("Provide a ticket number for parsing.")
            exitProcess(0)
        }
    }
}
