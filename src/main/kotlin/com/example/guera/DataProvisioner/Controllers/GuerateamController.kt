package com.example.guera.DataProvisioner.Controllers

import com.example.guera.DataProvisioner.Exceptions.BadRequestException
import com.example.guera.DataProvisioner.Exceptions.NotFoundException
import com.example.guera.DataProvisioner.Extensions.asJsonNode
import com.example.guera.DataProvisioner.Extensions.expectedProperties
import com.example.guera.DataProvisioner.Extensions.toModel
import com.example.guera.DataProvisioner.Extensions.toUUID
import com.example.guera.DataProvisioner.Interfaces.IGuerateamController
import com.example.guera.DataProvisioner.Interfaces.IGuerateamService
import com.example.guera.DataProvisioner.Models.*
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

    override fun subscribe(json: JsonNode): String = changeSubscription(json, true)

    override fun unsubscribe(json: JsonNode): String = changeSubscription(json, false)

    private fun changeSubscription(json: JsonNode, subscribe: Boolean): String {
        if (!json.has("id") || !json.has("userId"))
            throw BadRequestException(*Guerateam::class.expectedProperties("id", "userId"))
        val id = json["id"].textValue().toUUID()
        val userId = json["userId"].textValue()
        val team = guerateamService.find(id) ?: throw NotFoundException("Guerateam", id.toString())
        val members = if (subscribe) team.membersId + userId else team.membersId - userId
        val updated = team.copy(membersId = members)
        val success = guerateamService.modify(updated)
        val message = if (subscribe) "Subscription" else "Unsubscription"
        return if (success) Success(null).toString() else Failure("$message Failure").toString()
    }
}