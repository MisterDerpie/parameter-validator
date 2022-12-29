package com.misterderpie.parametervalidator.kotlin.model

class ValidationException(reason: String) : Exception(reason)

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Validator(
    val name: String
)
