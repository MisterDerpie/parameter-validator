package com.misterderpie.parametervalidator.kotlin.engine

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ParameterValidatorFactoryTest {
    private val validatorResolver = mockk<ValidatorResolver>()
    private val defaultValidatorName = "test"

    @Test
    fun `when exactly one method with two arguments is found then it returns that method`() {
        val factory = setupFactory(TestValidatorProper::class.java)
        val method = factory.getValidateMethod(defaultValidatorName)
        assertEquals("a", method.parameters[1].name)
        assertEquals("b", method.parameters[2].name)
    }

    @Test
    fun `when no validate method found then it throws a validator not found exception`() {
        val factory = setupFactory(TestValidatorNoValidate::class.java)
        assertThrows<ValidateMethodNotFoundException> { factory.getValidateMethod("test") }
    }

    @Test
    fun `when validate method not unique then it throws validate not unique exception`() {
        val factory = setupFactory(TestValidatorValidateNotUnique::class.java)
        assertThrows<ValidateMethodNotUniqueException> { factory.getValidateMethod("test") }
    }

    private fun setupFactory(
        validator: Class<out Any>,
        validatorName: String = defaultValidatorName
    ): ParameterValidatorFactory {
        every { validatorResolver.validatorsMap } returns mapOf(validatorName to validator)
        every { validatorResolver.getValidator(validatorName) } returns validator
        return ParameterValidatorFactory(validatorResolver)
    }

    class TestValidatorNoValidate

    class TestValidatorProper {
        fun validate(a: Int, b: Int) {
            println("$a$b")
        }
    }

    class TestValidatorValidateNotUnique {
        fun validate(a: Int, b: Int) {
            println("$a$b")
        }

        fun validate(a: Int, b: Float) {
            println("$a$b")
        }
    }
}
