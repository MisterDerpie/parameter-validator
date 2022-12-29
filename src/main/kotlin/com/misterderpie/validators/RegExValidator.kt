package com.misterderpie.validators

import com.misterderpie.parametervalidator.kotlin.model.ValidationException
import com.misterderpie.parametervalidator.kotlin.model.Validator

data class RegExParameters(val pattern: String)

@Validator(name = "regEx")
class RegExValidator {
    fun validate(value: String, parameters: RegExParameters) {
        if (!parameters.pattern.toRegex().matches(value)) {
            throw ValidationException("Parameter $value does not match pattern ${parameters.pattern}")
        }
    }
}
