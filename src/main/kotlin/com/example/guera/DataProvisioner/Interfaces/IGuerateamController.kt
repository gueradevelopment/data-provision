package com.example.guera.DataProvisioner.Interfaces

import com.fasterxml.jackson.databind.JsonNode

interface IGuerateamController: ICrudController {

    fun subscribe(json: JsonNode): String
    fun unsubscribe(json: JsonNode): String

}