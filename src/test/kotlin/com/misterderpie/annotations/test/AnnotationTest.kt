package com.misterderpie.annotations.test

import org.junit.jupiter.api.Test
import org.reflections.Reflections
import kotlin.reflect.full.primaryConstructor
import kotlin.test.assertEquals

class AnnotationTest {

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.CLASS)
    annotation class MyAnnotation(val value: String)

    @MyAnnotation(value = "test")
    class AnnotatedClass(val input: String)

    @Test
    fun `resolve annotation`() {
        val reflection = Reflections("com.misterderpie")
        val result = reflection.getTypesAnnotatedWith(MyAnnotation::class.java)

        assertEquals(1, result.size)

        val annotatedClass = result.toList()[0]
        val annotatedValue = annotatedClass.getAnnotation(MyAnnotation::class.java).value
        assertEquals("test", annotatedValue)

        val primaryConstructor = annotatedClass.kotlin.primaryConstructor!!
        val constructorParameters = primaryConstructor.parameters
        val out = constructorParameters.map { it to "1" }.toMap()

        val instance = primaryConstructor.callBy(out) as AnnotatedClass
        assertEquals("1", instance.input)
    }
}
