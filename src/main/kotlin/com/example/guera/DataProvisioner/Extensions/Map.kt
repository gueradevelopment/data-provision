package com.example.guera.DataProvisioner.Extensions

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode

val <V> Map<String, V>.json: JsonNode
    get() {
        val json = ObjectNode(JsonNodeFactory(false))
        for ((k, v) in this) {
            when(v) {
                is String -> json.put(k, v)
                is Int -> json.put(k, v)
                is Double -> json.put(k, v)
                is Long -> json.put(k, v)
                is Boolean -> json.put(k, v)
                else -> json.putPOJO(k, v)
            }
        }
        return json
    }