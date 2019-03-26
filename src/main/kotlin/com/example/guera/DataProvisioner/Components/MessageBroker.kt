package com.example.guera.DataProvisioner.Components

import com.example.guera.DataProvisioner.Exceptions.DataProvisionException
import com.example.guera.DataProvisioner.Interfaces.IGuerabookController
import com.example.guera.DataProvisioner.Models.Checklist
import com.example.guera.DataProvisioner.Models.Failure

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import khttp.post
import java.lang.Exception

@Component("MessageBroker")
class MessageBroker(
    @Autowired private val exchange: TopicExchange,
    @Autowired private val template: RabbitTemplate,
    @Autowired private val guerabookController: IGuerabookController
) : MessageListener {

    companion object {
        val queueName = "spring"
        val topicExchangeName = "spring-exchange"
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

    fun route(routingKey: String, message: String): String {
        println("Routing key: $routingKey")
        println("Message: $message")
        val json = ObjectMapper().readTree(message)
        return when (routingKey) {
            "create.guerabook" -> guerabookController.create(json)
            "retrieve.guerabook" -> guerabookController.retrieve(json)
            "update.guerabook" -> guerabookController.update(json)
            "delete.guerabook" -> guerabookController.delete(json)
            "retrieveAll.guerabook" -> guerabookController.retrieveAll()
            "retrieveAllId.guerabook" -> guerabookController.retrieveAllId()
            else -> "NotFound"
        }
    }

    fun addChecklist(json: JsonNode): String {
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
    }

}