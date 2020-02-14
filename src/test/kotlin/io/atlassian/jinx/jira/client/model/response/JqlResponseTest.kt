package io.atlassian.jinx.jira.client.model.response

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.io.StringReader
import javax.json.Json

class JqlResponseTest {

    @Test
    fun shouldCreateObjectFromJson(){
        val searchResponse = this.javaClass.getResource("/searchResponse.json").readText()
        val searchResponseJson = Json.createReader(StringReader(searchResponse)).readObject()

        val jqlResponse = JqlResponse.fromJson(searchResponseJson)

        Assertions.assertThat(jqlResponse.total).isEqualTo(24991)
    }

}