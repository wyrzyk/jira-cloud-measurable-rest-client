package io.atlassian.jinx.jira.client

import com.atlassian.performance.tools.jiraactions.api.ActionMetric
import com.atlassian.performance.tools.jiraactions.api.ActionResult
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.measure.output.CollectionActionMetricOutput
import com.atlassian.performance.tools.jiraactions.api.w3c.DisabledW3cPerformanceTimeline
import io.atlassian.jinx.jira.client.model.IssueType
import io.atlassian.jinx.jira.client.model.Project
import io.atlassian.jinx.jira.client.model.request.IssueRequest
import io.atlassian.jinx.jira.client.model.request.JqlRequest
import org.apache.http.HttpHeaders
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.net.URI
import java.time.Clock
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IssueClientIT {
    private val email = System.getenv("JIRA_EMAIL")
    private val apiToken = System.getenv("JIRA_API_TOKEN")
    private val jiraUri = URI(System.getenv("JIRA_URI"))
    private val httpClient = HttpClients
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

    @AfterAll
    fun cleanup() {
        httpClient.close()
    }

    @Test
    fun shouldCreateIssues() {
        val issue = IssueRequest(
            summary = "test-" + UUID.randomUUID().toString(),
            description = "test description",
            project = Project("TES"),
            issueType = IssueType(10001)
        )
        val actionMetrics = mutableListOf<ActionMetric>()
        val actionMeter = ActionMeter(
            virtualUser = UUID.randomUUID(),
            clock = Clock.systemUTC(),
            w3cPerformanceTimeline = DisabledW3cPerformanceTimeline(),
            output = CollectionActionMetricOutput(
                actionMetrics
            )
        )
        val jiraClient = JiraClient(
            httpClient,
            jiraUri,
            actionMeter
        )

        jiraClient.issue.create(issue)

        Assertions.assertThat(actionMetrics).hasSize(1)
        Assertions.assertThat(actionMetrics.first().result).isEqualTo(ActionResult.OK)
        Assertions.assertThat(actionMetrics.first().label).isEqualTo("CREATE_ISSUE_REST")
    }

    @Test
    fun shouldSearchForIssues() {
        val jqlRequest = JqlRequest(
            jql = "ORDER BY id DESC"
        )
        val actionMetrics = mutableListOf<ActionMetric>()
        val actionMeter = ActionMeter(
            virtualUser = UUID.randomUUID(),
            clock = Clock.systemUTC(),
            w3cPerformanceTimeline = DisabledW3cPerformanceTimeline(),
            output = CollectionActionMetricOutput(
                actionMetrics
            )
        )
        val jiraClient = JiraClient(
            httpClient,
            jiraUri,
            actionMeter
        )

        val response = jiraClient.search.find(jqlRequest)

        Assertions.assertThat(actionMetrics).hasSize(1)
        Assertions.assertThat(actionMetrics.first().result).isEqualTo(ActionResult.OK)
        Assertions.assertThat(actionMetrics.first().label).isEqualTo("SEARCH_REST")
        Assertions.assertThat(response.total).isGreaterThan(0)
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
