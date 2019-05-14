package com.example.guera.DataProvisioner.Components

import com.example.guera.DataProvisioner.Exceptions.DataProvisionException
import com.example.guera.DataProvisioner.Exceptions.UnsupportedActionException
import com.example.guera.DataProvisioner.Extensions.isTeamContext
import com.example.guera.DataProvisioner.Extensions.userId
import com.example.guera.DataProvisioner.Interfaces.*
import com.example.guera.DataProvisioner.Models.Failure
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
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
    @Autowired private val taskController: ITaskController,
    @Autowired private val guerateamController: IGuerateamController
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
            Failure(e.message ?: "Error", e::class.simpleName ?: "UnknownException").toString()
        } catch (e: IndexOutOfBoundsException) {
            Failure("Incorrect routing key format. Should be <type>.<object>.<action>", "BadRequestException").toString()
        } catch (e: Exception) {
            e.cause?.printStackTrace() ?: e.printStackTrace()
            Failure("Internal Server Failure", "InternalServerError").toString()
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
        val (context, resource, action) = routingKey.split(".")
        val json = ObjectMapper().readTree(message)
        (json as ObjectNode).put("isTeamContext", context.toBoolean())
        return when (resource) {
            "guerabook" -> bookRoute(action, json)
            "board" -> boardRoute(action, json)
            "checklist" -> checklistRoute(action, json)
            "task" -> taskRoute(action, json)
            "guerateam" -> teamRoute(action, json)
            else -> throw UnsupportedActionException(action, resource)
        }
    }

    private fun bookRoute(action: String, json: JsonNode): String = when(action) {
        "create" -> guerabookController.create(json)
        "retrieve" -> guerabookController.retrieve(json)
        "update" -> guerabookController.update(json)
        "delete" -> guerabookController.delete(json)
        "retrieveAll" -> guerabookController.retrieveAll(json.userId, json.isTeamContext)
        "retrieveAllId" -> guerabookController.retrieveAllId(json.userId, json.isTeamContext)
        else -> throw UnsupportedActionException(action, "Guerabook")
    }

    private fun boardRoute(action: String, json: JsonNode): String = when(action) {
        "create" -> boardController.create(json)
        "retrieve" -> boardController.retrieve(json)
        "update" -> boardController.update(json)
        "delete" -> boardController.delete(json)
        "retrieveAll" -> boardController.retrieveAll(json.userId, json.isTeamContext)
        "retrieveAllId" -> boardController.retrieveAllId(json.userId, json.isTeamContext)
        else -> throw UnsupportedActionException(action, "Board")
    }

    private fun checklistRoute(action: String, json: JsonNode): String = when(action) {
        "create" -> checklistController.create(json)
        "retrieve" -> checklistController.retrieve(json)
        "update" -> checklistController.update(json)
        "delete" -> checklistController.delete(json)
        "retrieveAll" -> checklistController.retrieveAll(json.userId, json.isTeamContext)
        "retrieveAllId" -> checklistController.retrieveAllId(json.userId, json.isTeamContext)
        "markComplete" -> checklistController.markAsComplete(json)
        else -> throw UnsupportedActionException(action, "Checklist")
    }

    private fun taskRoute(action: String, json: JsonNode): String = when(action) {
        "create" -> taskController.create(json)
        "retrieve" -> taskController.retrieve(json)
        "update" -> taskController.update(json)
        "delete" -> taskController.delete(json)
        "retrieveAll" -> taskController.retrieveAll(json.userId, json.isTeamContext)
        "retrieveAllId" -> taskController.retrieveAllId(json.userId, json.isTeamContext)
        "markComplete" -> checklistController.markAsComplete(json)
        else -> throw UnsupportedActionException(action, "Task")
    }

    private fun teamRoute(action: String, json: JsonNode): String = when(action) {
        "create" -> guerateamController.create(json)
        "retrieve" -> guerateamController.retrieve(json)
        "update" -> guerateamController.update(json)
        "delete" -> guerateamController.delete(json)
        "retrieveAll" -> guerateamController.retrieveAll(json.userId, json.isTeamContext)
        "retrieveAllId" -> guerateamController.retrieveAllId(json.userId, json.isTeamContext)
        "subscribe" -> guerateamController.subscribe(json)
        "unsubscribe" -> guerateamController.unsubscribe(json)
        else -> throw UnsupportedActionException(action, "Guerateam")
    }

}