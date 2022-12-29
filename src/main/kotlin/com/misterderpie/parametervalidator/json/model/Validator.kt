package com.misterderpie.parametervalidator.json.model

import com.fasterxml.jackson.databind.JsonNode

data class Validator(val name: String, val parameters: JsonNode)
