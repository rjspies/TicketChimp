# TicketChimp

TicketChimp is a Kotlin Native CLI app that allows you to easily generate Git branches locally by providing Jira tickets. This tool simplifies the process of creating branches corresponding to Jira issues in your Git repository.

## Prerequisites

- An existing Jira instance (e.g., `company.atlassian.net`).
- Git installed on your system.

## Download

You can download the pre-built binary for your platform from the [Releases](https://github.com/rjspies/TicketChimp/releases) page.

## Setup

To set up TicketChimp, use the following command:

```bash
tchimp -s
```

You will be guided through the setup process, which includes:

1. Providing the domain of your Jira instance (e.g., `company.atlassian.net`).
2. Specifying the authentication method to be used (`basic` for Basic Authentication or `bearer` for Bearer Token Authentication).
3. If using Basic Authentication, providing your Jira username and the name of the environment variable that holds your Jira token. If using Bearer Token Authentication, providing the environment variable name that holds the token.
4. Providing the local path to the Git repository where you want to generate the branches.

## Usage

After setting up TicketChimp, you can use it by providing the Jira ticket key as follows:

```bash
tchimp foo-420
```

This will generate a Git branch with the following naming convention for the Jira ticket `FOO-420`:

- If the ticket type is "User Story," "Developer Story," or "Spike," the branch name will be: `Feature/FOO-420+This-is-the-summary`.
- If the ticket type is "Bug," the branch name will be: `Bugfix/FOO-420+This-is-the-summary`.
- If the ticket type is none of the above, the branch name will be: `FOO-420+This-is-the-summary`.

### Contributing

Contributions to this project are welcome! If you have any ideas, suggestions, or bug reports, please feel free to open an issue or submit a pull request.
