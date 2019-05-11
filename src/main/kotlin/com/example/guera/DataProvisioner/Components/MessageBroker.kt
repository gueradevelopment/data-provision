package com.example.guera.DataProvisioner.Components

import com.example.guera.DataProvisioner.Exceptions.DataProvisionException
import com.example.guera.DataProvisioner.Exceptions.UnsupportedActionException
import com.example.guera.DataProvisioner.Extensions.userId
import com.example.guera.DataProvisioner.Interfaces.IBoardController
import com.example.guera.DataProvisioner.Interfaces.IChecklistController
import com.example.guera.DataProvisioner.Interfaces.IGuerabookController
import com.example.guera.DataProvisioner.Interfaces.ITaskController
import com.example.guera.DataProvisioner.Models.Failure
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener
import org.springframework.amqp.core.MessagePostProcessor
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("MessageBroker")
class MessageBroker(
    @Autowired private val template: RabbitTemplate,
    @Autowired private val guerabookController: IGuerabookController,
    @Autowired private val boardController: IBoardController,
    @Autowired private val checklistController: IChecklistController,
    @Autowired private val taskController: ITaskController
) : MessageListener {

    companion object {
        const val queueName = "main"
        const val topicExchangeName = "data-provision"
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
        println(message.messageProperties)
        println("Replying to: ${message.messageProperties.replyTo}")

        val postProcessor = MessagePostProcessor {
            it.messageProperties.correlationId = message.messageProperties.correlationId
            return@MessagePostProcessor it
        }

        template.convertAndSend("", message.messageProperties.replyTo, response, postProcessor)
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
        "retrieveAll" -> guerabookController.retrieveAll(json.userId)
        "retrieveAllId" -> guerabookController.retrieveAllId(json.userId)
        else -> throw UnsupportedActionException(action, "Guerabook")
    }

    private fun boardRoute(action: String, json: JsonNode): String = when(action) {
        "create" -> boardController.create(json)
        "retrieve" -> boardController.retrieve(json)
        "update" -> boardController.update(json)
        "delete" -> boardController.delete(json)
        "retrieveAll" -> boardController.retrieveAll(json.userId)
        "retrieveAllId" -> boardController.retrieveAllId(json.userId)
        else -> throw UnsupportedActionException(action, "Board")
    }

    private fun checklistRoute(action: String, json: JsonNode): String = when(action) {
        "create" -> checklistController.create(json)
        "retrieve" -> checklistController.retrieve(json)
        "update" -> checklistController.update(json)
        "delete" -> checklistController.delete(json)
        "retrieveAll" -> checklistController.retrieveAll(json.userId)
        "retrieveAllId" -> checklistController.retrieveAllId(json.userId)
        "markComplete" -> checklistController.markAsComplete(json)
        else -> throw UnsupportedActionException(action, "Checklist")
    }

    private fun taskRoute(action: String, json: JsonNode): String = when(action) {
        "create" -> taskController.create(json)
        "retrieve" -> taskController.retrieve(json)
        "update" -> taskController.update(json)
        "delete" -> taskController.delete(json)
        "retrieveAll" -> taskController.retrieveAll(json.userId)
        "retrieveAllId" -> taskController.retrieveAllId(json.userId)
        "markComplete" -> checklistController.markAsComplete(json)
        else -> throw UnsupportedActionException(action, "Task")
    }

}