package io.atlassian.jinx.jira.client.resources

import com.atlassian.performance.tools.jiraactions.api.ActionType
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import io.atlassian.jinx.jira.client.model.request.JqlRequest
import io.atlassian.jinx.jira.client.model.response.JqlResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import java.io.StringReader
import java.net.URI
import javax.json.Json

class SearchResource internal constructor(
    private val httpClient: HttpClient,
    jiraUri: URI,
    private val actionMeter: ActionMeter
) {
    private val searchAction = ActionType("SEARCH_REST") {}
    private val resource = jiraUri.resolve("/rest/api/3/search")

    fun find(jqlRequest: JqlRequest): JqlResponse {
        val searchRequest = HttpPost(resource)
        searchRequest.entity = StringEntity(
            jqlRequest.toJson().toString(),
            ContentType.APPLICATION_JSON
        )
        val response = actionMeter.measure(searchAction) {
            val response = httpClient.execute(searchRequest)
            if (response.statusLine.statusCode == 200) {
                return@measure response
            } else {
                throw RuntimeException("Action failed with status code ${response.statusLine.statusCode}.")
            }
        }

        val responseJson = Json.createReader(
            StringReader(
                response
                    .entity
                    .content
                    .bufferedReader()
                    .readText()
            )
        ).readObject()
        return JqlResponse.fromJson(responseJson)
    }
}