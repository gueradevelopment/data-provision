package com.example.guera.DataProvisioner.Interfaces

import com.fasterxml.jackson.databind.JsonNode

interface IChecklistController : ICrudController {
    fun markAsComplete(json: JsonNode): String
}