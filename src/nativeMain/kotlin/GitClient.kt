import platform.posix.system

private const val ID_USER_STORY = "User Story"
private const val ID_DEVELOPER_STORY = "Developer Story"
private const val ID_SPIKE = "Spike"
private const val ID_BUG = "Bug"

class GitClient {
    private val IssueType.prefix
        get() = when (name) {
            ID_USER_STORY,
            ID_DEVELOPER_STORY,
            ID_SPIKE,
            -> "Feature"
            ID_BUG -> "Bugfix"
            else -> null
        }

    private fun String.normalize() = toRegex().run {
        replace("[^a-zA-Z0-9_]", "")
        toString().replace(" ", "-")
    }

    fun createBranchFromIssue(repository: String, issue: Issue) {
        val branchName = "${issue.fields.issueType.prefix}/${issue.key}+${issue.fields.summary.normalize()}"

        system("cd $repository && git branch $branchName").also {
            if (it == 0) println("Created \"$branchName\"")
        }
    }
}
