package com.misterderpie.validator

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ValidatorFactoryTest {

    @Test
    fun `when create validator that cannot be found then throws ValidatorNotFoundException`() {
        val validatorName = "HeWhoMustNotBeNamed"
        val result = assertThrows<ValidatorNotFoundException> {
            ValidatorFactory.createValidator(validatorName)
        }
        assertEquals("No class found for validator: $validatorName", result.message)
    }
}