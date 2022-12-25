package com.misterderpie.parametervalidator.model

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TemplateTest {

    val gson = GsonBuilder().create()
    val template = """
{
    "name":"EncyclopediaArticleRequestValidation",
    "parameters":[
        {
            "name":"articleId",
            "required":true,
            "validators":[
                {
                    "name":"regEx",
                    "parameters":{
                        "pattern":"[a-z]"
                    }
                }
            ]
        }
    ]
}
    """.trimIndent()

    fun JsonObject.getRequired(name: String): JsonElement {
        return get(name) ?: throw Exception("Cannot find required element: $name")
    }

    @Test
    fun `test to traverse through template`() {
        val deserialized = gson.fromJson(template, JsonObject::class.java)
        val templateName = deserialized.getRequired("name").asString
        assertEquals("EncyclopediaArticleRequestValidation", templateName)
        val parameters = deserialized.getRequired("parameters").asJsonArray

        parameters.map { it.asJsonObject }.forEach { parameter ->
            val parameterName = parameter.getRequired("name").asString
            assertEquals("articleId", parameterName)
            val required = parameter.getRequired("required").asBoolean
            assertEquals(true, required)
            val validators = parameter.getRequired("validators").asJsonArray
            validators.map { it.asJsonObject }.forEach { validator ->
                val validatorName = validator.getRequired("name").asString
                val validatorParameters = validator.getRequired("parameters").asJsonObject
                assertEquals("regEx", validatorName)
                assertEquals(validatorParameters.getRequired("pattern").asString, "[a-z]")
            }
        }
    }
}