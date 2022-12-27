package com.misterderpie.jackson.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.misterderpie.parametervalidator.model.ValidationException
import com.misterderpie.parametervalidator.model.Validator
import com.misterderpie.test.createKotlinMapper
import com.misterderpie.validators.MaxLengthValidator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure
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
        val parametersType = maxLengthClass.primaryConstructor!!.parameters.get(0).type.jvmErasure.java

        val parametersInstance = objectMapper.treeToValue(parametersJsonNode, parametersType)

        val constructor = maxLengthClass.primaryConstructor ?: throw Exception("No primary constructor found")
        val instance = constructor.call(parametersInstance)

        assertDoesNotThrow { instance.validate("123") }
        assertThrows<ValidationException> { instance.validate("0".repeat(11)) }
    }
}