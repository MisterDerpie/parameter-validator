package com.misterderpie.jackson.test

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class JacksonKotlinModuleTest {
    private val objectMapper = jacksonObjectMapper().enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)

    @Target(AnnotationTarget.FUNCTION)
    annotation class TestAnnotation
    data class TestClass(val name: String, val age: Int) {
        @TestAnnotation
        fun testFun(name: String) { println(name) }
    }

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

    @Test
    fun `when getting parameter type from function argument jackson extracts it to it`() {
        val firstParameterType = TestClass::class.java.methods.first { it.name == "testFun" }.parameters.first().type

        val node = objectMapper.readTree("""{"name":"a"}""")
        val value = node.get("name")
        val instantiatedValue = objectMapper.treeToValue(value, firstParameterType)

        assertEquals(String::class.java, firstParameterType)
        assertEquals(String::class.java, instantiatedValue::class.java)
        assertEquals("a", instantiatedValue)
    }
}
