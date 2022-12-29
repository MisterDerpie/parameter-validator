package com.misterderpie.validators

import com.misterderpie.parametervalidator.kotlin.model.ValidationException
import com.misterderpie.parametervalidator.kotlin.model.Validator

data class SetOfParameters(val set: Set<String>)
@Validator(name = "setOf")
class SetOfValidator {
    fun validate(value: String, setOfParameters: SetOfParameters) {
        if (!setOfParameters.set.contains(value)) {
            val allowedValues = setOfParameters.set.joinToString()
            throw ValidationException("Value $value not allowed. Allowed: $allowedValues")
        }
    }
}
