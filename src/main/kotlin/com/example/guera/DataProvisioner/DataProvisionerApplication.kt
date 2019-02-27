package com.example.guera.DataProvisioner


import com.example.guera.DataProvisioner.Components.MessageBroker
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class DataProvisionerApplication {

    @Bean
    fun queue() = Queue(MessageBroker.queueName, false)

    @Bean
    fun topicExchange() = TopicExchange(MessageBroker.topicExchangeName)

    @Bean
    fun binding(queue: Queue, topicExchange: TopicExchange) = BindingBuilder
        .bind(queue).to(topicExchange)
        .with("guera.#")

    @Bean
    fun container(
        connectionFactory: ConnectionFactory,
        broker: MessageBroker
    ): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.setMessageListener(broker)
        container.setQueueNames(MessageBroker.queueName)
        return container
    }

}

fun main(args: Array<String>) {
    val context = runApplication<DataProvisionerApplication>(*args)
}
