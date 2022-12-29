import com.misterderpie.parametervalidator.ParameterValidator
import com.misterderpie.parametervalidator.json.engine.TemplateParser
import com.misterderpie.parametervalidator.kotlin.engine.ParameterValidatorFactory
import com.misterderpie.parametervalidator.kotlin.engine.ValidatorResolver

fun main(args: Array<String>) {
    val template = TemplateParser.fromTemplate(articleTemplate)
    val validatorResolver = ValidatorResolver("com.misterderpie.validators")
    val parameterValidatorFactory = ParameterValidatorFactory(validatorResolver)
    val parameterValidator = ParameterValidator(parameterValidatorFactory)

    println("This is the validator example. The template used is the same as in the README.md")
    println("Please provide values for")
    val articleId = input("articleId: ")
    var language: String? = null
    if (input("Provide language? y/N: ").lowercase() == "y") {
        language = input("language: ")
    }

    val parameters = mutableMapOf(
        "articleId" to articleId
    )
    language?.let { parameters.put("language", it) }

    parameterValidator.validate(parameters, template)

    println("Success, the input was accepted")
}

private fun input(message: String): String {
    print(message)
    return readln()
}

private val articleTemplate = """
{
    "name":"EncyclopediaArticleRequestValidation",
    "parameters":{
        "articleId":{
            "required":true,
            "validators":[
                {
                    "name":"regEx",
                    "parameters":{
                        "pattern":"[a-z]+"
                    }
                },
                {
                    "name":"minLength",
                    "parameters":{
                        "length":1
                    }
                },
                {
                    "name":"maxLength",
                    "parameters":{
                        "length":10
                    }
                }
            ]
        },
        "language":{
            "required":false,
            "validators":[
                {
                    "name":"setOf",
                    "parameters":{
                        "set":[
                            "english", "german", "irish"
                        ]
                    }
                }
            ]
        }
    }
}
""".trimIndent()
