package com.misterderpie.validator

class ValidatorNotFoundException(name: String) : Exception("No class found for validator: $name")


class ValidatorFactory {
    companion object {
        fun createValidator(name: String): Validator {
            try {
                val validatorClass = Class.forName(name)
                return validatorClass.getDeclaredConstructor().newInstance() as Validator
            } catch (ex: ClassNotFoundException) {
                throw ValidatorNotFoundException(name)
            }
        }
    }
}