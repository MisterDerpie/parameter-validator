package com.misterderpie.parametervalidator.kotlin.json.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.misterderpie.parametervalidator.json.engine.TemplateParser
import com.misterderpie.parametervalidator.json.model.Parameter
import com.misterderpie.parametervalidator.json.model.Template
import com.misterderpie.test.createKotlinMapper
import com.misterderpie.validators.MaxLengthValidatorParameters
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TemplateParserTest {
    private val objectMapper = ObjectMapper().createKotlinMapper()

    @Test
    fun `verify parsing from template works`() {
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
        val template: Template = TemplateParser.fromTemplate(json)
        assertEquals("Test", template.name)

        val parameters = template.parameters

        assertTrue { parameters.containsKey("articleId") }

        val articleIdParameter: Parameter = parameters["articleId"]!!
        assertEquals(1, articleIdParameter.validators.size)

        val validator = articleIdParameter.validators[0]
        assertEquals("maxLength", validator.name)

        val maxLengthParameters: MaxLengthValidatorParameters = objectMapper.treeToValue(validator.parameters)
        assertEquals(10, maxLengthParameters.length)
    }

    @Test
    fun `when validators not provided throws exception`() {
        val json = """
            {
                "name":"Test",
                "parameters":{
                    "articleId":{
                        "required":true
                    }
                }
            }"""
        val expectedErrorMessage =
            "value failed for JSON property validators due to missing (therefore NULL) value for " +
                "creator parameter validators which is a non-nullable type"
        assertThrows<MissingKotlinParameterException> { objectMapper.readValue(json, Template::class.java) }.also {
            println(expectedErrorMessage)
            assertContains(it.message!!, expectedErrorMessage)
        }
    }
}
