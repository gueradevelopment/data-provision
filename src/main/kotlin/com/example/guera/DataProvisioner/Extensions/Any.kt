package com.example.guera.DataProvisioner.Extensions

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.jvmErasure

fun Any.asJsonNode(key: String): JsonNode? = ObjectMapper().createObjectNode().put(key, this.toString())

fun <T: Any> KClass<T>.expectedProperties(vararg filter: String): Array<String> {
    return this.declaredMemberProperties
        .filter { it.name in filter.toList()}
        .map { "${it.name}: ${it.returnType.jvmErasure.simpleName}" }
        .toTypedArray()
}

fun <T> Optional<T>.unwrap(): T? = orElse(null)