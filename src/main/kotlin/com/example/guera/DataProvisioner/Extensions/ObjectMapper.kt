package com.example.guera.DataProvisioner.Extensions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ser.FilterProvider
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider

fun ObjectMapper.writeValueAsString(value: Any?, vararg ignoreFields: String): String {
    val filter = SimpleBeanPropertyFilter.serializeAllExcept(*ignoreFields)
    val filters = SimpleFilterProvider().addFilter("ignore", filter)
    return this.writer(filters).writeValueAsString(value)
}