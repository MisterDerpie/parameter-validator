package com.misterderpie.parametervalidator.kotlin.engine

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KFunction
import kotlin.reflect.cast
import kotlin.reflect.jvm.jvmErasure

class ValidatorInvoke {
    companion object {
        private val objectMapper = jacksonObjectMapper().enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
    }

    fun callValidateFunction(validateInstance: Any, validateMethod: KFunction<*>, value: Any, parameters: JsonNode) {
        val valueType = validateMethod.parameters[1]
        val parametersType = validateMethod.parameters[2]

        val valueArgument = valueType.type.jvmErasure.cast(value)
        val parametersArgument = objectMapper.treeToValue(parameters, parametersType.type.jvmErasure.java)

        try {
            validateMethod.call(validateInstance, valueArgument, parametersArgument)
        } catch (ex: InvocationTargetException) {
            throw ex.targetException
        }
    }
}
