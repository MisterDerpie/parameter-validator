package com.misterderpie.parametervalidator.json.engine

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.misterderpie.parametervalidator.json.model.Template

class TemplateParser {
    companion object {
        private val objectMapper = jacksonObjectMapper().enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
        fun fromTemplate(templateJson: String): Template {
            return objectMapper.readValue(templateJson)
        }
    }
}
