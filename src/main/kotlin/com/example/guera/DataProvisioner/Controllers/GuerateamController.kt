package com.example.guera.DataProvisioner.Controllers

import com.example.guera.DataProvisioner.Exceptions.BadRequestException
import com.example.guera.DataProvisioner.Exceptions.NotFoundException
import com.example.guera.DataProvisioner.Extensions.asJsonNode
import com.example.guera.DataProvisioner.Extensions.expectedProperties
import com.example.guera.DataProvisioner.Extensions.toModel
import com.example.guera.DataProvisioner.Interfaces.IGuerateamController
import com.example.guera.DataProvisioner.Interfaces.IGuerateamService
import com.example.guera.DataProvisioner.Models.Failure
import com.example.guera.DataProvisioner.Models.Guerateam
import com.example.guera.DataProvisioner.Models.Success
import com.example.guera.DataProvisioner.Models.Task
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import java.lang.IllegalArgumentException
import java.util.*

@Controller
class GuerateamController(
    @Autowired private val guerateamService: IGuerateamService
): IGuerateamController {

    override fun create(json: JsonNode): String {
        val team = json.toModel<Guerateam>("name", "userId")
        val id = guerateamService.add(team)
        return Success(id.asJsonNode("id")).toString()
    }

    override fun retrieve(json: JsonNode): String {
        if (!json.has("id")) throw BadRequestException(*Guerateam::class.expectedProperties("id"))
        val id = json["id"].textValue()
        val uuid = try { UUID.fromString(id) } catch (e: IllegalArgumentException) { throw BadRequestException("id") }
        val team = guerateamService.find(uuid)
        team ?: throw NotFoundException("Guerateam", id)
        return Success(team).toString()
    }

    override fun retrieveAll(userId: String): String {
        val teams = guerateamService.findAll(userId)
        return Success(teams).toString()
    }

    override fun retrieveAllId(userId: String): String {
        val idList = guerateamService.findAllId(userId)
        return Success(idList).toString()
    }

    override fun delete(json: JsonNode): String {
        if (!json.has("id")) throw BadRequestException(*Guerateam::class.expectedProperties("id"))
        val id = json["id"].textValue()
        val uuid = try { UUID.fromString(id) } catch (e: IllegalArgumentException) { throw BadRequestException(id) }
        val success = guerateamService.remove(uuid)
        return if (success) Success(null).toString() else Failure("Deletion Failure").toString()
    }

    override fun update(json: JsonNode): String {
        val team = json.toModel<Guerateam>()
        guerateamService.modify(team)
        return Success(null).toString()
    }
}