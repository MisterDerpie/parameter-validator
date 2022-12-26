package com.misterderpie.validators

import com.misterderpie.parametervalidator.model.AbstractValidator
import com.misterderpie.parametervalidator.model.Validator
import com.misterderpie.parametervalidator.model.ValidatorException
import com.misterderpie.parametervalidator.model.ValidatorParameters

data class MaxLengthValidatorParameters(val length: Int) : ValidatorParameters

@Validator(
    name = "maxLength",
    parametersType = MaxLengthValidatorParameters::class
)
class MaxLengthValidator(parameters: MaxLengthValidatorParameters) :
    AbstractValidator<MaxLengthValidatorParameters, String>(parameters) {
        override fun validate(value: String) {
            if (value.length > parameters.length) {
                throw ValidatorException("Maximum length of ${parameters.length} exceeded")
            }
        }
}