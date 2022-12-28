package com.misterderpie.parametervalidator.engine.unhappypath.validatenotunique

import com.misterderpie.parametervalidator.engine.ParameterValidatorFactory
import com.misterderpie.parametervalidator.engine.ValidateMethodNotUniqueException
import com.misterderpie.parametervalidator.model.Validator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@Validator("first")
class FirstValidator {
    fun validate(a: Int, b: Int) {
        println("$a$b")
    }

    fun validate(a: String, b: Int) {
        println("$a$b")
    }
}

class ParameterValidatorFactoryTest {

    @Test
    fun `when validate not unique throws validatenotunique exception`() {
        assertThrows<ValidateMethodNotUniqueException> {
            ParameterValidatorFactory("com.misterderpie.parametervalidator.engine.unhappypath.validatenotunique")
        }
    }
}
