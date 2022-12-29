package com.misterderpie.parametervalidator.kotlin.engine

import com.fasterxml.jackson.databind.ObjectMapper
import com.misterderpie.test.createKotlinMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.jvm.kotlinFunction

class TestException : Exception()
data class TestParameters(val length: Int)
class TestValidator {
    fun validate(value: String, parameters: TestParameters) {
        if (value.length > parameters.length) throw TestException()
    }
}

class ValidatorInvokeTest {
    private val objectMapper = ObjectMapper().createKotlinMapper()

    @Test
    fun `when validator invoke called with accepted input then it does not throw an exception`() {
        val parametersJson = """{"length":3}"""
        val parameters = objectMapper.readTree(parametersJson)
        val instance = TestValidator()
        val method = TestValidator::class.java.methods.first { it.name == "validate" }.kotlinFunction!!

        val invoke = ValidatorInvoke()
        assertDoesNotThrow { invoke.callValidateFunction(instance, method, "123", parameters) }
    }

    @Test
    fun `when validator invoke called with wrong input then it throws validator exception`() {
        val parametersJson = """{"length":3}"""
        val parameters = objectMapper.readTree(parametersJson)
        val instance = TestValidator()
        val method = TestValidator::class.java.methods.first { it.name == "validate" }.kotlinFunction!!

        val invoke = ValidatorInvoke()
        assertThrows<TestException> { invoke.callValidateFunction(instance, method, "a".repeat(11), parameters) }
    }
}
