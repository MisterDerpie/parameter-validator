package com.misterderpie.validators

import com.misterderpie.parametervalidator.kotlin.model.ValidationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class MaxLengthValidatorTest {

    @Test
    fun `when string provided is shorter than limit then accepts string`() {
        val input = "a"
        val parameters = ValidationLengthParameters(5)
        assertDoesNotThrow { MaxLengthValidator().validate(input, parameters) }
    }

    @Test
    fun `when string provided is longer than limit then throws exception`() {
        val input = "a".repeat(10)
        val parameters = ValidationLengthParameters(5)
        assertThrows<ValidationException> { MaxLengthValidator().validate(input, parameters) }
    }
}
