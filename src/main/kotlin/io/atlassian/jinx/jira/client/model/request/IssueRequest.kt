package io.atlassian.jinx.jira.client.model.request

import io.atlassian.jinx.jira.client.model.IssueType
import io.atlassian.jinx.jira.client.model.Project
import javax.json.Json
import javax.json.JsonObject
import javax.json.JsonObjectBuilder

data class IssueRequest(
    val summary: String,
    val description: String,
    val project: Project,
    val issueType: IssueType
) {
    fun toJson(): JsonObject {
        val fields = Json
            .createObjectBuilder()
            .add("summary", summary)
            .add("description", createDescription())
            .add("project", project.toJson())
            .add("issuetype", issueType.toJson())
        return Json.createObjectBuilder()
            .add("fields", fields)
            .build()
    }

    private fun createDescription(): JsonObjectBuilder {
        return Json
            .createObjectBuilder()
            .add("type", "doc")
            .add("version", 1)
            .add(
                "content",
                Json
                    .createArrayBuilder()
                    .add(
                        Json
                            .createObjectBuilder()
                            .add(
                                "content",
                                Json.createArrayBuilder()
                                    .add(
                                        Json
                                            .createObjectBuilder()
                                            .add("text", description)
                                            .add("type", "text")
                                    )
                            )
                            .add("type", "paragraph")
                    )
            )
    }
}
