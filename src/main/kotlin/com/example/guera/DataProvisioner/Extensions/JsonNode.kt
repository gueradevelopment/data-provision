package com.example.guera.DataProvisioner.Extensions

import com.example.guera.DataProvisioner.Exceptions.BadRequestException
import com.example.guera.DataProvisioner.Models.Guerabook
import com.example.guera.DataProvisioner.Models.Identified
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

inline fun <reified T: Identified> JsonNode.toModel(vararg fields: String): T = try {
    jacksonObjectMapper().treeToValue(this, T::class.java)
} catch (e: JsonProcessingException) {
    e.printStackTrace()
    val properties = T::class.expectedProperties(*fields)
    throw BadRequestException(*properties)
}

inline fun <reified T> JsonNode.toReq(): T = try {
    jacksonObjectMapper().treeToValue(this, T::class.java)
} catch (e: JsonProcessingException) {
    e.printStackTrace()
    throw BadRequestException("Could not parse request")
}

inline fun <T> JsonNode.property(name: String, cast: (JsonNode) -> T): T {
    val prop = if (this.has(name)) this[name] else throw BadRequestException(name)
    return cast(prop)
}

inline val JsonNode.userId: String
    get() = property("userId") { it.textValue() }

inline val JsonNode.isTeamContext: Boolean
    get() = property("isTeamContext") { it.booleanValue() }