package com.misterderpie.validators

import com.misterderpie.parametervalidator.model.ValidationException
import com.misterderpie.parametervalidator.model.Validator

data class MaxLengthValidatorParameters(val length: Int)

@Validator(name = "maxLength")
class MaxLengthValidator {
    fun validate(value: String, parameters: MaxLengthValidatorParameters) {
        if (value.length > parameters.length) {
            throw ValidationException("Maximum length of ${parameters.length} exceeded")
        }
    }
}
