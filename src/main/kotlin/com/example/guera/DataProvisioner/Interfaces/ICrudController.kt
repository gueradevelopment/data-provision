package com.example.guera.DataProvisioner.Interfaces

import com.fasterxml.jackson.databind.JsonNode

interface ICrudController {

    fun create(json: JsonNode): String
    fun retrieve(json: JsonNode): String
    fun retrieveAll(): String
    fun retrieveAllId(): String
    fun delete(json: JsonNode): String
    fun update(json: JsonNode): String

}