package io.atlassian.jinx.jira.client.model.request

import javax.json.Json
import javax.json.JsonObject

data class JqlRequest(
    val jql: String
) {
    fun toJson(): JsonObject {
        return Json
            .createObjectBuilder()
            .add("jql", jql)
            .build()
    }
}