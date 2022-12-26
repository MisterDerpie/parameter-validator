package com.misterderpie.jackson.test

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class JacksonKotlinModuleTest {
    private val objectMapper = jacksonObjectMapper().enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)

    data class TestClass(val name: String, val age: Int)

    @Test
    fun `when reading from json containing all values no exception is thrown`() {
        val json = """{"name":"misterderpie","age":26}"""
        val instance: TestClass = objectMapper.readValue(json, TestClass::class.java)

        assertEquals(instance.name, "misterderpie")
        assertEquals(instance.age, 26)
    }

    @Test
    fun `when reading from json not containing all values exception is thrown`() {
        val json = """{"name":"misterderpie"}"""
        assertThrows<MismatchedInputException> { objectMapper.readValue(json, TestClass::class.java) }
    }
}