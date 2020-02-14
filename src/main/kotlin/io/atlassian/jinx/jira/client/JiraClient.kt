package io.atlassian.jinx.jira.client

import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import io.atlassian.jinx.jira.client.model.ProjectIssueType
import io.atlassian.jinx.jira.client.resources.IssueResource
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import java.net.URI

class JiraClient(
    private val httpClient: HttpClient,
    private val jiraUri: URI,
    actionMeter: ActionMeter
) {
    val issue = IssueResource(
        httpClient,
        jiraUri,
        actionMeter
    )

    private fun fetchProjectIssueTypes(): Set<ProjectIssueType> {
        val fetchIssueTypesRequest = HttpGet(jiraUri.resolve("rest/api/3/issuetype"))
        val response = httpClient.execute(fetchIssueTypesRequest)
        throw RuntimeException("Not supported yet.")
    }
}
