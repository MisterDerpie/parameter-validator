package com.misterderpie.parametervalidator.engine

import com.misterderpie.parametervalidator.model.Validator
import org.reflections.Reflections
import java.util.logging.Logger
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.kotlinFunction

class ValidatorNotFoundException(name: String) : Exception("No validator found for name: $name")
class ValidateMethodNotFoundException(name: String) :
    Exception("Validator does not contain any validate method that takes exactly two parameters. Validator: $name")

class ValidateMethodNotUniqueException(name: String) :
    Exception("Validator must only contain one validate method with two parameters. Validator: $name")

class ParameterValidatorFactory(
    packagePrefix: String = "com.misterderpie.validators"
) {
    companion object {
        private val logger = Logger.getLogger(this::class.java.name)
    }

    private val validatorsMap: Map<String, Class<*>>
    private val validatorsMethodMap: Map<String, KFunction<*>>

    init {
        val reflection = Reflections(packagePrefix)
        validatorsMap = reflection.getTypesAnnotatedWith(Validator::class.java)
            .associateBy { it.getAnnotation(Validator::class.java).name }
        if (validatorsMap.isEmpty()) {
            logger.warning("ParameterValidatorMapper found 0 loggers in classPath: $packagePrefix")
        }

        validatorsMethodMap = validatorsMap.map {
            val method = getValidateMethod(it.key)
            it.key to method
        }.toMap()
    }

    fun getValidator(name: String): Class<*> {
        return validatorsMap[name] ?: throw ValidatorNotFoundException(name)
    }

    fun getValidateMethod(name: String): KFunction<*> {
        val validatorClass = getValidator(name)
        val validatorMethods = validatorClass.methods.filter { it.name == "validate" && it.parameters.size == 2 }

        val size = validatorMethods.size
        when {
            size == 0 -> throw ValidateMethodNotFoundException(name)
            size > 1 -> throw ValidateMethodNotUniqueException(name)
        }

        return validatorMethods.first().kotlinFunction!!
    }
}
