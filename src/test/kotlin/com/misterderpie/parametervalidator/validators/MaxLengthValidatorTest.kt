package com.misterderpie.parametervalidator.validators

import com.fasterxml.jackson.databind.ObjectMapper
import com.misterderpie.parametervalidator.model.Validator
import com.misterderpie.parametervalidator.model.ValidatorException
import com.misterderpie.test.createKotlinMapper
import com.misterderpie.validators.MaxLengthValidator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.test.assertEquals

class MaxLengthValidatorTest {

    val objectMapper = ObjectMapper().createKotlinMapper()

    @Test
    fun `instantiate from JSON template`() {
        val validatorJson = """{"name":"maxLength","parameters":{"length":5}}"""
        val validatorJsonNode = objectMapper.readTree(validatorJson)

        val nameNode = validatorJsonNode.get("name")
        val name = objectMapper.treeToValue(nameNode, String::class.java)
        assertEquals("maxLength", name)

        val parametersJsonNode = validatorJsonNode.get("parameters")

        val maxLengthClass = MaxLengthValidator::class
        val annotation = maxLengthClass.findAnnotation<Validator>() ?: throw Exception("Validator not present")
        val parametersClass = Class.forName(annotation.parametersType.qualifiedName)
        val parametersInstance = objectMapper.treeToValue(parametersJsonNode, parametersClass)

        val constructor = maxLengthClass.primaryConstructor ?: throw Exception("No primary constructor found")
        val instance = constructor.call(parametersInstance)

        assertDoesNotThrow { instance.validate("123") }
        assertThrows<ValidatorException> { instance.validate("0".repeat(11)) }
    }
}