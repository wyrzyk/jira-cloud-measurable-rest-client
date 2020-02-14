package io.atlassian.jinx.generator

import io.atlassian.jinx.jira.client.JiraClient
import io.atlassian.jinx.jira.client.model.Issue
import io.atlassian.jinx.jira.client.model.IssueType
import io.atlassian.jinx.jira.client.model.Project
import org.apache.http.HttpHeaders
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.net.URI
import java.util.*

class IssueClientIT {

    @Test
    fun shouldCreateIssues() {
        val email = System.getenv("JIRA_EMAIL")
        val apiToken = System.getenv("JIRA_API_TOKEN")
        val jiraUri = URI(System.getenv("JIRA_URI"))

        val issue = Issue(
            summary = "test-" + UUID.randomUUID().toString(),
            description = "test description",
            project = Project("TES"),
            issueType = IssueType(10001)
        )
        HttpClients
            .custom()
            .setDefaultHeaders(
                setOf(
                    getJiraAuthHeader(
                        email = email,
                        apiToken = apiToken
                    )
                )
            )
            .build()
            .use { httpClient ->
                val jiraClient = JiraClient(
                    httpClient,
                    jiraUri
                )

                Assertions.assertThat(jiraClient.issue.create(issue)).isTrue()
            }
    }

    private fun getJiraAuthHeader(
        email: String,
        apiToken: String
    ) =
        BasicHeader(
            HttpHeaders.AUTHORIZATION,
            "Basic " +
                    Base64
                        .getEncoder()
                        .encodeToString("$email:$apiToken".toByteArray())
        )
}
