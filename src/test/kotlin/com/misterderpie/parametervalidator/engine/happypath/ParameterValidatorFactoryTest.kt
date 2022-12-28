package com.misterderpie.parametervalidator.engine.happypath

import com.misterderpie.parametervalidator.engine.ParameterValidatorFactory
import com.misterderpie.parametervalidator.engine.ValidatorNotFoundException
import com.misterderpie.parametervalidator.model.Validator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

@Validator("first")
class FirstValidator {
    fun validate(a: Int, b: Int) {
        println("$a$b")
    }
}

@Validator("second")
class SecondValidator {
    fun validate(a: Int, b: Int) {
        println("$a$b")
    }
}

class ParameterValidatorFactoryTest {

    val defaultFactory = ParameterValidatorFactory("com.misterderpie.parametervalidator.engine.happypath")

    @Test
    fun `when class path provided it finds validators`() {
        assertEquals(FirstValidator::class.java, defaultFactory.getValidator("first"))
        assertEquals(SecondValidator::class.java, defaultFactory.getValidator("second"))
    }

    @Test
    fun `when no validators found it initializes correctly`() {
        assertDoesNotThrow { ParameterValidatorFactory("no.real.path") }
    }

    @Test
    fun `when validator requested that does not exist throws exception`() {
        val parameterValidatorFactory = ParameterValidatorFactory("no.real.path")
        assertThrows<ValidatorNotFoundException> { parameterValidatorFactory.getValidator("any") }
    }
}
