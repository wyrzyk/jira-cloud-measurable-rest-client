package io.atlassian.jinx.jira.client.model

import javax.json.Json
import javax.json.JsonObject

data class IssueType(
    val id: Int
) {
    fun toJson(): JsonObject {
        return Json
            .createObjectBuilder()
            .add("id", "$id")
            .build()
    }
}
