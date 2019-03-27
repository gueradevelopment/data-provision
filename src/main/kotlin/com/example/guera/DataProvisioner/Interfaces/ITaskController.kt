package com.example.guera.DataProvisioner.Interfaces

import com.fasterxml.jackson.databind.JsonNode

interface ITaskController : ICrudController {
    fun markAsComplete(json: JsonNode): String
}