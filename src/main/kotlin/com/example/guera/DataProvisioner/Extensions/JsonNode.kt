package com.example.guera.DataProvisioner.Extensions

import com.example.guera.DataProvisioner.Exceptions.BadRequestException
import com.example.guera.DataProvisioner.Models.Guerabook
import com.example.guera.DataProvisioner.Models.Identified
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

inline fun <reified T: Identified> JsonNode.toBean(vararg fields: String): T {
    val book = try {
        ObjectMapper().treeToValue(this, T::class.java)!!
    } catch (e: JsonProcessingException) {
        e.printStackTrace()
        val properties = Guerabook::class.expectedProperties(*fields)
        throw BadRequestException(*properties)
    }
    book.clear()
    return book
}