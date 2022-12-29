package com.misterderpie.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.misterderpie.parametervalidator.kotlin.model.ValidationException
import com.misterderpie.validators.MaxLengthValidator
import com.misterderpie.validators.ValidationLengthParameters
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.full.primaryConstructor
import kotlin.test.assertEquals

class MaxLengthValidatorJacksonTest {

    private val objectMapper = ObjectMapper().createKotlinMapper()

    @Test
    fun `instantiate from JSON template`() {
        val validatorJson = """{"name":"maxLength","parameters":{"length":5}}"""
        val validatorJsonNode = objectMapper.readTree(validatorJson)

        val nameNode = validatorJsonNode.get("name")
        val name = objectMapper.treeToValue(nameNode, String::class.java)
        assertEquals("maxLength", name)

        val parametersJsonNode = validatorJsonNode.get("parameters")

        val maxLengthClass = MaxLengthValidator::class
        val validateMethod = maxLengthClass.java.methods.first { it.name == "validate" }
        val parametersType = validateMethod.parameters.get(1).type

        val parametersInstance =
            objectMapper.treeToValue(parametersJsonNode, parametersType) as ValidationLengthParameters

        val constructor = maxLengthClass.primaryConstructor ?: throw Exception("No primary constructor found")
        val instance = constructor.call()

        assertDoesNotThrow { instance.validate("123", parametersInstance) }
        assertThrows<ValidationException> { instance.validate("0".repeat(11), parametersInstance) }
    }
}
