package io.atlassian.jinx.jira.client.resources

import com.atlassian.performance.tools.jiraactions.api.ActionType
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import io.atlassian.jinx.jira.client.model.Issue
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import java.net.URI

class IssueResource internal constructor(
    private val httpClient: HttpClient,
    jiraUri: URI,
    private val actionMeter: ActionMeter
) {
    private val createIssueAction = ActionType("CREATE_ISSUE_REST") {}
    private val resource = jiraUri.resolve("rest/api/3/issue")

    fun create(issue: Issue): Boolean {
        val createIssueRequest = HttpPost(resource)
        createIssueRequest.entity = StringEntity(
            issue.toJson().toString(),
            ContentType.APPLICATION_JSON
        )
        try {
            return actionMeter.measure(createIssueAction) {
                val response = httpClient.execute(createIssueRequest)
                if (response.statusLine.statusCode == 201) {
                    return@measure true
                } else {
                    throw RuntimeException("Action failed with status code ${response.statusLine.statusCode}.")
                }
            }
        } catch (e: Exception) {
            return false
        }
    }
}