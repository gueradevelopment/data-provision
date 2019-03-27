package com.example.guera.DataProvisioner.Components

import com.example.guera.DataProvisioner.Controllers.ChecklistController
import com.example.guera.DataProvisioner.Exceptions.DataProvisionException
import com.example.guera.DataProvisioner.Exceptions.UnsupportedActionException
import com.example.guera.DataProvisioner.Interfaces.IBoardController
import com.example.guera.DataProvisioner.Interfaces.IChecklistController
import com.example.guera.DataProvisioner.Interfaces.IGuerabookController
import com.example.guera.DataProvisioner.Interfaces.ITaskController
import com.example.guera.DataProvisioner.Models.Checklist
import com.example.guera.DataProvisioner.Models.Failure

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import khttp.post
import java.lang.Exception

@Component("MessageBroker")
class MessageBroker(
    @Autowired private val template: RabbitTemplate,
    @Autowired private val guerabookController: IGuerabookController,
    @Autowired private val boardController: IBoardController,
    @Autowired private val checklistController: IChecklistController,
    @Autowired private val taskController: ITaskController
) : MessageListener {

    companion object {
        const val queueName = "spring"
        const val topicExchangeName = "spring-exchange"
    }

    override fun onMessage(message: Message) {
        val payload = message.body.toString(Charsets.UTF_8)
        val response = try {
            route(message.messageProperties.receivedRoutingKey, payload)
        } catch (e: DataProvisionException) {
            Failure(e.message ?: "Error").toString()
        } catch (e: Exception) {
            e.cause?.printStackTrace() ?: e.printStackTrace()
            Failure("Internal Server Failure").toString()
        }
        template.convertAndSend(message.messageProperties.replyTo, response)
    }

    private fun route(routingKey: String, message: String): String {
        val (resource, action) = routingKey.split(".")
        val json = ObjectMapper().readTree(message)
        return when (resource) {
            "guerabook" -> bookRoute(action, json)
            "board" -> boardRoute(action, json)
            "checklist" -> checklistRoute(action, json)
            "task" -> taskRoute(action, json)
            else -> throw UnsupportedActionException(action, resource)
        }
    }

    private fun bookRoute(action: String, json: JsonNode): String = when(action) {
        "create" -> guerabookController.create(json)
        "retrieve" -> guerabookController.retrieve(json)
        "update" -> guerabookController.update(json)
        "delete" -> guerabookController.delete(json)
        "retrieveAll" -> guerabookController.retrieveAll()
        "retrieveAllId" -> guerabookController.retrieveAllId()
        else -> throw UnsupportedActionException(action, "Guerabook")
    }

    private fun boardRoute(action: String, json: JsonNode): String = when(action) {
        "create" -> boardController.create(json)
        "retrieve" -> boardController.retrieve(json)
        "update" -> boardController.update(json)
        "delete" -> boardController.delete(json)
        "retrieveAll" -> boardController.retrieveAll()
        "retrieveAllId" -> boardController.retrieveAllId()
        else -> throw UnsupportedActionException(action, "Board")
    }

    private fun checklistRoute(action: String, json: JsonNode): String = when(action) {
        "create" -> checklistController.create(json)
        "retrieve" -> checklistController.retrieve(json)
        "update" -> checklistController.update(json)
        "delete" -> checklistController.delete(json)
        "retrieveAll" -> checklistController.retrieveAll()
        "retrieveAllId" -> checklistController.retrieveAllId()
        "markComplete" -> checklistController.markAsComplete(json)
        else -> throw UnsupportedActionException(action, "Checklist")
    }

    private fun taskRoute(action: String, json: JsonNode): String = when(action) {
        "create" -> taskController.create(json)
        "retrieve" -> taskController.retrieve(json)
        "update" -> taskController.update(json)
        "delete" -> taskController.delete(json)
        "retrieveAll" -> taskController.retrieveAll()
        "retrieveAllId" -> taskController.retrieveAllId()
        "markComplete" -> checklistController.markAsComplete(json)
        else -> throw UnsupportedActionException(action, "Task")
    }

/*    fun addChecklist(json: JsonNode): String {
        val token = json["token"].toString()
        return if (verifyAuth(token)) {
            val checklist = Checklist(
                title = json["title"].toString(),
                description = json["description"].toString(),
                board = null
            )
            //checklistService.addChecklist(checklist)
            "Success"
        } else {
            "Failure"
        }
    }

    fun verifyAuth(token: String): Boolean {
        val response = post("localhost:8080/verify", data = mapOf(
            "token" to token
        ))
        return response.statusCode == 200
    }*/

}