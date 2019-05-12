package com.example.guera.DataProvisioner.Extensions

import com.example.guera.DataProvisioner.Exceptions.BadRequestException
import java.lang.IllegalArgumentException
import java.util.*

fun String.toUUID() = try {
    UUID.fromString(this)
} catch (e: IllegalArgumentException) {
    throw BadRequestException("id")
}
