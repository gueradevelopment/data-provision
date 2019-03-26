package com.example.guera.DataProvisioner.Models

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode

open class Message(val type: String)

data class Success(val data: Any?): Message("success") {

    override fun toString(): String {
        val mapper = ObjectMapper()
        val json = mapper.createObjectNode()
            .put("type", type)

        when(data) {
            null -> json.putObject("data")
            is List<*> -> {
                json.putArray("data")
                data.forEach {
                    val array = json["data"] as ArrayNode
                    val str = it as? String
                    if (str != null) {
                        array.add(str)
                    } else {
                        array.addPOJO(it)
                    }
                }
            }
            else -> json.putPOJO("data", data)
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json)
    }

}

data class Failure(val reason: String): Message("error") {

    override fun toString(): String {
        val mapper = ObjectMapper()
        val json = mapper.createObjectNode()
            .put("type", type)
            .put("reason", reason)

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json)
    }

}

