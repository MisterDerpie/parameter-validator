package com.misterderpie.validators

import com.misterderpie.parametervalidator.kotlin.model.ValidationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class MinLengthValidatorTest {
    @Test
    fun `when string provided is longer than limit then throws acc`() {
        val input = "a".repeat(10)
        val parameters = ValidationLengthParameters(5)
        assertDoesNotThrow { MinLengthValidator().validate(input, parameters) }
    }

    @Test
    fun `when string provided is longer than limit then throws exception`() {
        val input = "a"
        val parameters = ValidationLengthParameters(5)
        assertThrows<ValidationException> { MinLengthValidator().validate(input, parameters) }
    }
}
