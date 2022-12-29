package com.misterderpie.parametervalidator.kotlin.engine

import kotlin.reflect.KFunction
import kotlin.reflect.jvm.kotlinFunction

class ValidateMethodNotFoundException(name: String) :
    Exception("Validator does not contain any validate method that takes exactly two parameters. Validator: $name")

class ValidateMethodNotUniqueException(name: String) :
    Exception("Validator must only contain one validate method with two parameters. Validator: $name")

class ParameterValidatorFactory(
    private val validatorResolver: ValidatorResolver
) {

    fun getValidatorInstance(name: String): Any {
        return validatorResolver.getValidator(name).instance
    }
    fun getValidateMethod(name: String): KFunction<*> {
        val validatorClass = validatorResolver.getValidator(name).clazz
        val validatorMethods = validatorClass.methods.filter { it.name == "validate" && it.parameters.size == 2 }

        val size = validatorMethods.size
        when {
            size == 0 -> throw ValidateMethodNotFoundException(name)
            size > 1 -> throw ValidateMethodNotUniqueException(name)
        }

        return validatorMethods.first().kotlinFunction!!
    }
}
