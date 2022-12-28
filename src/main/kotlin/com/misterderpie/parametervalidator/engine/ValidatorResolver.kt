package com.misterderpie.parametervalidator.engine

import com.misterderpie.parametervalidator.model.Validator
import org.reflections.Reflections
import java.util.logging.Logger

class ValidatorNotFoundException(name: String) : Exception("No validator found for name: $name")
class ValidatorResolver(packagePrefix: String) {
    companion object {
        private val logger = Logger.getLogger(this::class.java.name)
    }

    val validatorsMap: Map<String, Class<*>>

    init {
        val reflection = Reflections(packagePrefix)
        validatorsMap = reflection.getTypesAnnotatedWith(Validator::class.java)
            .associateBy { it.getAnnotation(Validator::class.java).name }

        if (validatorsMap.isEmpty()) {
            logger.warning("ValidatorResolver found 0 loggers in classPath: $packagePrefix")
        }
    }

    fun getValidator(name: String): Class<*> {
        return validatorsMap[name] ?: throw ValidatorNotFoundException(name)
    }
}
