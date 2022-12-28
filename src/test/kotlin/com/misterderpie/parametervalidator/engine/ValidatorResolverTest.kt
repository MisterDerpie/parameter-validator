package com.misterderpie.parametervalidator.engine

import com.misterderpie.parametervalidator.model.Validator
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
        val validatorResolver = ValidatorResolver("com.misterderpie.parametervalidator.engine")
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
