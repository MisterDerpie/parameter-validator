package com.misterderpie.parametervalidator.json.model

import com.fasterxml.jackson.databind.JsonNode

data class Parameter(val required: Boolean, val default: JsonNode?, val validators: List<Validator>)
