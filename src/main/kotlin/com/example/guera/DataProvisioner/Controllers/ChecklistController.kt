package com.example.guera.DataProvisioner.Controllers

import com.example.guera.DataProvisioner.Exceptions.BadRequestException
import com.example.guera.DataProvisioner.Exceptions.NotFoundException
import com.example.guera.DataProvisioner.Extensions.asJsonNode
import com.example.guera.DataProvisioner.Extensions.expectedProperties
import com.example.guera.DataProvisioner.Extensions.toModel
import com.example.guera.DataProvisioner.Interfaces.IChecklistController
import com.example.guera.DataProvisioner.Interfaces.IChecklistService
import com.example.guera.DataProvisioner.Models.Checklist
import com.example.guera.DataProvisioner.Models.Failure
import com.example.guera.DataProvisioner.Models.Success
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

import java.util.*
import java.lang.IllegalArgumentException

@Controller
class ChecklistController(
    @Autowired private val checklistService: IChecklistService
): IChecklistController {

    override fun create(json: JsonNode): String {
        val checklist = json.toModel<Checklist>("title", "userId", "isTeamContext", "boardId")
        val bookId = json["boardId"]
        val id = if (bookId != null) {
            val uuid = UUID.fromString(bookId.textValue())
            checklistService.add(checklist.copy(boardId = uuid.toString()), uuid)
        } else checklistService.add(checklist)
        return Success(id.asJsonNode("id")).toString()
    }

    override fun retrieve(json: JsonNode): String {
        if (!json.has("id")) throw BadRequestException(*Checklist::class.expectedProperties("id"))
        val id = json["id"].textValue()
        val uuid = try { UUID.fromString(id) } catch (e: IllegalArgumentException) { throw BadRequestException("id") }
        val checklist = checklistService.find(uuid)
        checklist ?: throw NotFoundException("Board", id)
        return Success(checklist).toString()
    }

    override fun retrieveAll(userId: String, isTeamContext: Boolean): String {
        val checklists = checklistService.findAll(userId, isTeamContext)
        return Success(checklists).toString()
    }

    override fun retrieveAllId(userId: String, isTeamContext: Boolean): String {
        val idList = checklistService.findAllId(userId, isTeamContext)
        return Success(idList).toString()
    }

    override fun delete(json: JsonNode): String {
        if (!json.has("id")) throw BadRequestException(*Checklist::class.expectedProperties("id"))
        val id = json["id"].textValue()
        val uuid = try { UUID.fromString(id) } catch (e: IllegalArgumentException) { throw BadRequestException("id") }
        val success = checklistService.remove(uuid)
        return if (success) Success(null).toString() else Failure("Deletion Failure", "InternalServerError").toString()
    }

    override fun update(json: JsonNode): String {
        val checklist = json.toModel<Checklist>("id")
        checklistService.modify(checklist)
        return Success(null).toString()
    }

    override fun markAsComplete(json: JsonNode): String {
        if (!json.has("id")) throw BadRequestException(*Checklist::class.expectedProperties("id"))
        val id = json["id"].textValue()
        val uuid = try { UUID.fromString(id) } catch (e: IllegalArgumentException) { throw BadRequestException("id") }
        val date = checklistService.markAsComplete(uuid)
        val response = date?.asJsonNode("date")
        return if (response != null) Success(response).toString()
        else Failure("Id of Checklist does not exist or it has already been marked as completed", "NotFoundException").toString()
    }
}