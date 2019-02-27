package com.example.guera.DataProvisioner.Components

import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("MessageBroker")
class MessageBroker(
    @Autowired private val exchange: TopicExchange
) : MessageListener {



    companion object {
        val queueName = "spring"
        val topicExchangeName = "spring-exchange"
    }

    override fun onMessage(message: Message) {
        val payload = message.body.toString(Charsets.UTF_8)
        val response = route(message.messageProperties.receivedRoutingKey, payload)
    }

    fun route(routingKey: String, message: String): String {
        println("Routing key: $routingKey")
        println("Message: $message")
        return when (routingKey) {
            "task" -> handleTask("")
            else -> "NotFound"
        }
    }

    fun handleTask(message: String): String {
        return ""
    }

}