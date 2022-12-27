package com.misterderpie.parametervalidator.engine.fakepath

import com.misterderpie.parametervalidator.engine.ParameterValidatorFactory
import com.misterderpie.parametervalidator.engine.ValidatorNotFoundException
import com.misterderpie.parametervalidator.model.Validator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

@Validator("first")
class FirstValidator

@Validator("second")
class SecondValidator

class ParameterValidatorFactoryTest {

    @Test
    fun `when class path provided it finds validators`() {
        val parameterValidatorFactory =
            ParameterValidatorFactory("com.misterderpie.parametervalidator.engine.fakepath")
        assertEquals(FirstValidator::class.java, parameterValidatorFactory.getValidator("first"))
        assertEquals(SecondValidator::class.java, parameterValidatorFactory.getValidator("second"))
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
