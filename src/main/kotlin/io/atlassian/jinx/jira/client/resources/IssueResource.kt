package io.atlassian.jinx.jira.client.resources

import io.atlassian.jinx.jira.client.model.Issue
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import java.net.URI

class IssueResource internal constructor(
    private val httpClient: HttpClient,
    jiraUri: URI
) {
    private val resource = jiraUri.resolve("rest/api/3/issue")

    fun create(issue: Issue): Boolean {
        val createIssueRequest = HttpPost(resource)
        createIssueRequest.entity = StringEntity(
            issue.toJson().toString(),
            ContentType.APPLICATION_JSON
        )
        try {
            val response = httpClient.execute(createIssueRequest)
            return response.statusLine.statusCode == 201
        } catch (e: Exception) {
            return false
        }
    }
}