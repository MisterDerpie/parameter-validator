package com.misterderpie.parametervalidator

import com.misterderpie.parametervalidator.json.model.ParameterConfiguration
import com.misterderpie.parametervalidator.json.model.Template
import com.misterderpie.parametervalidator.kotlin.engine.ParameterValidatorFactory
import com.misterderpie.parametervalidator.kotlin.engine.ValidatorInvoke

class ParameterNotFoundException(name: String) : Exception("Could not find parameter $name that is marked as required")

class ParameterValidator(
    private val validatorFactory: ParameterValidatorFactory,
    private val validatorInvoke: ValidatorInvoke = ValidatorInvoke()
) {

    fun validate(
        parameters: Map<String, Any>,
        validatorTemplate: Template,
        factory: ParameterValidatorFactory
    ) {
        validatorTemplate.parameters.forEach { configuration ->
            val name = configuration.key
            val parameterConfiguration = configuration.value
            when (parameterConfiguration.required) {
                true -> validate(getRequiredParameter(name, parameters), parameterConfiguration, factory)
                false -> parameters[name]?.let { validate(it, parameterConfiguration, factory) }
            }
        }
    }

    private fun getRequiredParameter(name: String, parameters: Map<String, Any>): Any {
        return parameters[name] ?: throw ParameterNotFoundException(name)
    }

    private fun validate(
        value: Any,
        parameterConfiguration: ParameterConfiguration,
        factory: ParameterValidatorFactory
    ) {
        parameterConfiguration.validators.forEach {
            val validatorInstance = factory.getValidatorInstance(it.name)
            val validateMethod = factory.getValidateMethod(it.name)
            validatorInvoke.callValidateFunction(validatorInstance, validateMethod, value, it.parameters)
        }
    }
}
