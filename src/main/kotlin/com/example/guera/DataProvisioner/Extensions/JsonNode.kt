package com.example.guera.DataProvisioner.Extensions

import com.example.guera.DataProvisioner.Exceptions.BadRequestException
import com.example.guera.DataProvisioner.Models.Guerabook
import com.example.guera.DataProvisioner.Models.Identified
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

inline fun <reified T: Identified> JsonNode.toModel(vararg fields: String): T {
    val model = try {
        ObjectMapper().treeToValue(this, T::class.java)!!
    } catch (e: JsonProcessingException) {
        e.printStackTrace()
        val properties = T::class.expectedProperties(*fields)
        throw BadRequestException(*properties)
    }
    return model
}