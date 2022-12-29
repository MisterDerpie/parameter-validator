package com.misterderpie.parametervalidator.kotlin.engine

import com.misterderpie.parametervalidator.kotlin.model.Validator
import org.reflections.Reflections
import java.util.logging.Logger
import kotlin.reflect.full.createInstance

class ValidatorNotFoundException(name: String) : Exception("No validator found for name: $name")
class ValidatorResolver(packagePrefix: String) {
    data class ValidatorInstance(val clazz: Class<*>, val instance: Any)
    companion object {
        private val logger = Logger.getLogger(this::class.java.name)
    }

    val validatorsMap: Map<String, ValidatorInstance>

    init {
        val reflection = Reflections(packagePrefix)
        validatorsMap = reflection.getTypesAnnotatedWith(Validator::class.java).associate {
            it.getAnnotation(Validator::class.java).name to ValidatorInstance(it, it.kotlin.createInstance())
        }

        if (validatorsMap.isEmpty()) {
            logger.warning("ValidatorResolver found 0 loggers in classPath: $packagePrefix")
        }
    }

    fun getValidator(name: String): ValidatorInstance {
        return validatorsMap[name] ?: throw ValidatorNotFoundException(name)
    }
}
