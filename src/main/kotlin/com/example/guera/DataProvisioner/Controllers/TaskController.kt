package com.example.guera.DataProvisioner.Controllers

import com.example.guera.DataProvisioner.Exceptions.BadRequestException
import com.example.guera.DataProvisioner.Exceptions.NotFoundException
import com.example.guera.DataProvisioner.Extensions.asJsonNode
import com.example.guera.DataProvisioner.Extensions.expectedProperties
import com.example.guera.DataProvisioner.Extensions.toModel
import com.example.guera.DataProvisioner.Extensions.toReq
import com.example.guera.DataProvisioner.Interfaces.ITaskController
import com.example.guera.DataProvisioner.Interfaces.ITaskService
import com.example.guera.DataProvisioner.Models.*
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

import java.lang.IllegalArgumentException
import java.util.*

@Controller
class TaskController(
    @Autowired private val taskService: ITaskService
): ITaskController {

    override fun create(json: JsonNode): String {
        val task = json.toModel<Task>("title", "userId", "checklistId")
        val checklistId = json["checklistId"]
        val id = if (checklistId != null) {
            val uuid = UUID.fromString(checklistId.textValue())
            taskService.add(task.copy(parentId = uuid.toString()), uuid)
        } else taskService.add(task)
        return Success(id.asJsonNode("id")).toString()
    }

    override fun retrieve(json: JsonNode): String {
        if (!json.has("id")) throw BadRequestException(*Task::class.expectedProperties("id"))
        val id = json["id"].textValue()
        val uuid = try { UUID.fromString(id) } catch (e: IllegalArgumentException) { throw BadRequestException("id") }
        val board = taskService.find(uuid)
        board ?: throw NotFoundException("Board", id)
        return Success(board).toString()
    }

    override fun retrieveAll(userId: String, isTeamContext: Boolean): String {
        val tasks = taskService.findAll(userId, isTeamContext)
        return Success(tasks).toString()
    }

    override fun retrieveAllId(userId: String, isTeamContext: Boolean): String {
        val idList = taskService.findAllId(userId, isTeamContext)
        return Success(idList).toString()
    }

    override fun delete(json: JsonNode): String {
        if (!json.has("id")) throw BadRequestException(*Task::class.expectedProperties("id"))
        val id = json["id"].textValue()
        val uuid = try { UUID.fromString(id) } catch (e: IllegalArgumentException) { throw BadRequestException("id") }
        val success = taskService.remove(uuid)
        return if (success) Success(null).toString() else Failure("Deletion Failure").toString()
    }

    override fun update(json: JsonNode): String {
        val request = json.toReq<TaskReq>()
        val task = taskService.find(request.id) ?: throw NotFoundException("Task", request.id.toString())
        val query = task.copy(
            title = request.title ?: task.title,
            description = request.description ?: task.description,
            completionState = request.completionState ?: task.completionState
        )
        val success = taskService.modify(query)
        return if (success) Success(null).toString() else throw NotFoundException("Task", task.id.toString())
    }

    override fun markAsComplete(json: JsonNode): String {
        if (!json.has("id")) throw BadRequestException(*Task::class.expectedProperties("id"))
        val id = json["id"].textValue()
        val uuid = try { UUID.fromString(id) } catch (e: IllegalArgumentException) { throw BadRequestException("id") }
        val date = taskService.markAsComplete(uuid)
        val response = date?.asJsonNode("date")
        return if (response != null) Success(response).toString()
        else Failure("Id of Checklist does not exist or it has already been marked as completed").toString()
    }

}