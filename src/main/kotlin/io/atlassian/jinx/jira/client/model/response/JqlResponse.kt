package io.atlassian.jinx.jira.client.model.response

import javax.json.JsonObject

class JqlResponse private constructor(
    val total: Int
) {
    internal companion object {
        internal fun fromJson(json: JsonObject): JqlResponse {
            val total = json.getInt("total")

            return JqlResponse(total)
        }
    }
}