package com.misterderpie.parametervalidator.json.model

import com.fasterxml.jackson.databind.JsonNode

data class Template(val name: String, val parameters: Map<String, JsonNode>)
