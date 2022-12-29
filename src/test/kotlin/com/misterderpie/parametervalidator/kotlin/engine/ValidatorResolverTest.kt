package com.misterderpie.parametervalidator.kotlin.engine

import com.misterderpie.parametervalidator.kotlin.model.Validator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

@Validator(name = "myValidator")
class MyValidator

@Validator(name = "mySecondValidator")
class MySecondValidator

class ValidatorResolverTest {

    @Test
    fun `when providing package path to resolver then it finds validators`() {
        val validatorResolver = ValidatorResolver("com.misterderpie.parametervalidator.kotlin.engine")
        val myValidator = validatorResolver.getValidator("myValidator")
        val mySecondValidator = validatorResolver.getValidator("mySecondValidator")

        assertEquals(MyValidator::class.java, myValidator)
        assertEquals(MySecondValidator::class.java, mySecondValidator)
    }

    @Test
    fun `when validator not found then throws validator not found exception`() {
        val validatorResolver = ValidatorResolver("some.fake.path")
        assertThrows<ValidatorNotFoundException> { validatorResolver.getValidator("any") }
    }
}
