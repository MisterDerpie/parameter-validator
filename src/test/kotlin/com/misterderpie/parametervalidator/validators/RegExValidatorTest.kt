package com.misterderpie.parametervalidator.validators

import com.misterderpie.parametervalidator.kotlin.model.ValidationException
import com.misterderpie.validators.RegExParameters
import com.misterderpie.validators.RegExValidator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class RegExValidatorTest {

    @Test
    fun `when string provided matches pattern then accepts string`() {
        val input = "ab"
        val parameters = RegExParameters("[a-z]{2}")
        assertDoesNotThrow { RegExValidator().validate(input, parameters) }
    }

    @Test
    fun `when string provided is longer than limit then throws exception`() {
        val input = "abc"
        val parameters = RegExParameters("[a-z]{2}")
        assertThrows<ValidationException> { RegExValidator().validate(input, parameters) }
    }
}
