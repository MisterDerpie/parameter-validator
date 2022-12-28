package com.misterderpie.parametervalidator.engine.unhappypath.validatenotfound

import com.misterderpie.parametervalidator.engine.ParameterValidatorFactory
import com.misterderpie.parametervalidator.engine.ValidateMethodNotFoundException
import com.misterderpie.parametervalidator.model.Validator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@Validator("first")
class FirstValidator

class ParameterValidatorFactoryTest {

    @Test
    fun `when validator does not contain validate method then validatemethodnotfound exception is thrown`() {
        assertThrows<ValidateMethodNotFoundException> {
            ParameterValidatorFactory("com.misterderpie.parametervalidator.engine.unhappypath")
        }
    }
}
