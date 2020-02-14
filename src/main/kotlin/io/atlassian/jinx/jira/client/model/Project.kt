package io.atlassian.jinx.jira.client.model

import javax.json.Json
import javax.json.JsonObject

data class Project(val key: String) {
    fun toJson(): JsonObject {
        return Json
            .createObjectBuilder()
            .add("key", key)
            .build()
    }
}
