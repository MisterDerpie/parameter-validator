package com.misterderpie.validators

import com.misterderpie.parametervalidator.kotlin.model.ValidationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class SetOfValidatorTest {

    @Test
    fun `when string provided is inside set then accepts string`() {
        val input = "german"
        val parameters = SetOfParameters(setOf("german", "english", "irish"))
        assertDoesNotThrow { SetOfValidator().validate(input, parameters) }
    }

    @Test
    fun `when string provided is longer than limit then throws exception`() {
        val input = "french"
        val parameters = SetOfParameters(setOf("german", "english", "irish"))
        assertThrows<ValidationException> { SetOfValidator().validate(input, parameters) }
    }
}
