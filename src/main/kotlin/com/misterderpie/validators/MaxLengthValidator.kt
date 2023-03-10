package com.misterderpie.validators

import com.misterderpie.parametervalidator.kotlin.model.ValidationException
import com.misterderpie.parametervalidator.kotlin.model.Validator

@Validator(name = "maxLength")
class MaxLengthValidator {
    fun validate(value: String, parameters: ValidationLengthParameters) {
        if (value.length > parameters.length) {
            throw ValidationException("Maximum length of ${parameters.length} exceeded")
        }
    }
}
