package io.atlassian.jinx.jira.client

import io.atlassian.jinx.jira.client.model.ProjectIssueType
import io.atlassian.jinx.jira.client.resources.IssueResource
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import java.net.URI

class JiraClient(
    private val httpClient: HttpClient,
    private val jiraUri: URI
) {
    val issue = IssueResource(
        httpClient,
        jiraUri
    )

    private fun fetchProjectIssueTypes(): Set<ProjectIssueType> {
        val fetchIssueTypesRequest = HttpGet(jiraUri.resolve("rest/api/3/issuetype"))
        val response = httpClient.execute(fetchIssueTypesRequest)
        throw RuntimeException("Not supported yet.")
    }
}
