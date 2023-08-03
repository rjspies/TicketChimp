import platform.posix.system

class Git {
    private val IssueType.prefix
        get() = when (name) {
            "User story",
            "Developer story",
            "Spike",
            -> "Feature"
            "Bug" -> "Bugfix"
            else -> null
        }

    private fun String.normalize() = toRegex().run {
        replace("[^a-zA-Z0-9_]", "")
        toString().replace(" ", "-")
    }

    fun createBranchFromIssue(repository: String, issue: Issue) {
        system("cd $repository")
        system("git branch ${issue.fields.issueType.prefix}/${issue.key}+${issue.fields.summary.normalize()}")
    }
}
