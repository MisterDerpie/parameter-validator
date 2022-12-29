package com.misterderpie.parametervalidator.kotlin.paramval

import com.misterderpie.parametervalidator.ParameterValidator
import com.misterderpie.parametervalidator.json.engine.TemplateParser
import com.misterderpie.parametervalidator.kotlin.engine.ParameterValidatorFactory
import com.misterderpie.parametervalidator.kotlin.engine.ValidatorResolver
import com.misterderpie.parametervalidator.kotlin.model.ValidationException
import com.misterderpie.parametervalidator.kotlin.model.Validator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

data class TestLengthParameter(val length: Int)

@Validator(name = "maxLength")
class MaxLengthValidator {
    fun validate(value: String, parameters: TestLengthParameter) {
        if (value.length > parameters.length) {
            throw ValidationException("Too long")
        }
    }
}

@Validator(name = "minLength")
class MinLengthValidator {
    fun validate(value: String, parameters: TestLengthParameter) {
        if (value.length < parameters.length) {
            throw ValidationException("Too short")
        }
    }
}

class ParameterValidatorTest {
    private val templateJson = """
            {
                "name":"Test",
                "parameters":{
                    "test":{
                        "required":true,
                        "validators":[
                            {
                                "name":"maxLength",
                                "parameters":{
                                    "length":10
                                }
                            },
                            {
                                "name":"minLength",
                                "parameters":{
                                    "length":3
                                }
                            }
                        ]
                    }
                }
            }"""
    private val defaultPackagePrefix = "com.misterderpie.parametervalidator.kotlin.paramval"
    private val defaultValidatorResolver = ValidatorResolver(defaultPackagePrefix)
    private val defaultValidatorFactory = ParameterValidatorFactory(defaultValidatorResolver)
    private val defaultTemplate = TemplateParser.fromTemplate(templateJson)
    private val defaultParameterValidator = ParameterValidator(defaultValidatorFactory)

    @Test
    fun `when validator invoked with accepted value it will not throw exception`() {
        val parameters = mapOf("test" to "value")
        assertDoesNotThrow { defaultParameterValidator.validate(parameters, defaultTemplate) }
    }

    @ParameterizedTest(name = "Validation fails for length with shortest 3 longest 10 for: {0}")
    @ValueSource(strings = ["a", "0123456789000"])
    fun `when value provided that validation should fail for then it fails`(parameter: String) {
        val parameters = mapOf("test" to parameter)
        assertThrows<ValidationException> {
            defaultParameterValidator.validate(
                parameters,
                defaultTemplate
            )
        }
    }

    private val nonRequiredParameterJson = """
            {
                "name":"Test",
                "parameters":{
                    "test":{
                        "required":false,
                        "validators":[
                            {
                                "name":"maxLength",
                                "parameters":{
                                    "length":10
                                }
                            },
                            {
                                "name":"minLength",
                                "parameters":{
                                    "length":3
                                }
                            }
                        ]
                    }
                }
            }"""

    @Test
    fun `when parameter not required then it will pass validation`() {
        val template = TemplateParser.fromTemplate(nonRequiredParameterJson)
        assertDoesNotThrow { defaultParameterValidator.validate(emptyMap(), template) }
    }

    @Test
    fun `when non required parameter provided it will validate it and fail`() {
        val template = TemplateParser.fromTemplate(nonRequiredParameterJson)
        assertThrows<ValidationException> {
            defaultParameterValidator.validate(
                mapOf("test" to "a".repeat(11)),
                template
            )
        }
    }
}
