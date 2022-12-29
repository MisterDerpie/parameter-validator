package com.misterderpie.parametervalidator.kotlin.json.model

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.misterderpie.parametervalidator.json.model.Parameter
import com.misterderpie.parametervalidator.json.model.Template
import com.misterderpie.test.createKotlinMapper
import com.misterderpie.validators.MaxLengthValidatorParameters
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParameterTest {
    private val objectMapper = ObjectMapper().createKotlinMapper()

    @Test
    fun `verify that parameter can be parsed with object mapper`() {
        val json = """
            {
                "name":"Test",
                "parameters":{
                    "articleId":{
                        "required":true,
                        "validators":[
                            {
                                "name":"maxLength",
                                "parameters":{
                                    "length":10
                                }
                            }
                        ]
                    }
                }
            }"""
        val template: Template = objectMapper.readValue(json)
        assertEquals("Test", template.name)

        val parameters: Map<String, JsonNode> = template.parameters

        assertTrue { parameters.containsKey("articleId") }

        val articleIdParameter: Parameter = objectMapper.treeToValue(parameters["articleId"]!!)
        assertEquals(1, articleIdParameter.validators.size)

        val validator = articleIdParameter.validators[0]
        assertEquals("maxLength", validator.name)

        val maxLengthParameters: MaxLengthValidatorParameters = objectMapper.treeToValue(validator.parameters)
        assertEquals(10, maxLengthParameters.length)
    }
}
