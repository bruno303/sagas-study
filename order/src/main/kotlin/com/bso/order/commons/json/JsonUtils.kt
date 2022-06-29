package com.bso.order.commons.json

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class JsonUtils(
    val objectMapper: ObjectMapper
) {
    final inline fun <reified T> fromJson(json: String): T = objectMapper.readValue(json, T::class.java)
    final fun toJson(objectToJson: Any): String = objectMapper.writeValueAsString(objectToJson)
}
