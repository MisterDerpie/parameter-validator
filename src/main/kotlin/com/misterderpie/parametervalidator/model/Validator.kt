package com.misterderpie.parametervalidator.model

import kotlin.reflect.KClass

class ValidatorException(reason: String) : Exception(reason)

abstract class AbstractValidator<out T : ValidatorParameters, I>(val parameters: T) {
    abstract fun validate(value: I)
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Validator(
    val name: String,
    val parametersType: KClass<out ValidatorParameters>
)
