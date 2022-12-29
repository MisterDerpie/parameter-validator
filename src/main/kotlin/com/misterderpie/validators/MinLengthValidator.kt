package com.misterderpie.validators

import com.misterderpie.parametervalidator.kotlin.model.ValidationException
import com.misterderpie.parametervalidator.kotlin.model.Validator

@Validator(name = "minLength")
class MinLengthValidator {

    fun validate(value: String, parameters: ValidationLengthParameters) {
        if (value.length < parameters.length) {
            throw ValidationException("Value too short. Minimum length: ${parameters.length}")
        }
    }
}
