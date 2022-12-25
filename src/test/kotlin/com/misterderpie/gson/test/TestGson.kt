package com.misterderpie.gson.test

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestGson {

    private val gson = GsonBuilder().create()

    class ParameterClass(val parameters: Map<String, Any>)

    @Test
    fun `when getting parameter that is string casting to string is safe`() {
        val json = """{"parameters":{"pattern":"[a-z]"}}"""
        val map = gson.fromJson(json, ParameterClass::class.java)
        val pattern = map.parameters["pattern"] as String

        assertEquals("[a-z]", pattern)
    }

    @Test
    fun `when getting parameter that is set casting to list is safe`() {
        val json = """{"parameters":{"setOf":["a","b","c"]}}"""
        val map = gson.fromJson(json, ParameterClass::class.java)
        // Below is necessary to avoid Unsafe Cast, as List<String>
        val set = (map.parameters["setOf"] as List<*>).filterIsInstance<String>().toSet()

        assertEquals(setOf("a", "b", "c"), set)
    }

    @Test
    fun `when getting parameter that is int casting to int is safe`() {
        val json = """{"parameters":{"length":5}}"""
        val map = gson.fromJson(json, ParameterClass::class.java)
        val length = (map.parameters["length"] as Double).toInt()

        assertEquals(5, length)
    }

    @Test
    fun `when specifying parameters body it will deserialize correctly`() {
        val json = """{"parameters":{"length":5}}"""
        val parameters = gson.fromJson(json, JsonObject::class.java)
        val parametersObject= parameters.get("parameters")

        class LengthParameter(val length: Int)
        val lengthParameter = gson.fromJson(parametersObject, LengthParameter::class.java)

        assertEquals(lengthParameter.length, 5)
    }

    @Test
    fun `when specifying set then gson converts list to set`() {
        val json = """{"parameters":{"setOf":["a","b","c"]}}"""
        val parameters = gson.fromJson(json, JsonObject::class.java)
        val parametersObject = parameters.get("parameters")

        class SetOf(val setOf: Set<String>)
        val set = gson.fromJson(parametersObject, SetOf::class.java)

        assertEquals(setOf("a", "b", "c"), set.setOf)
    }
}