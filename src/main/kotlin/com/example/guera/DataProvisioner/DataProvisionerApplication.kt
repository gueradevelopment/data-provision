package com.example.guera.DataProvisioner


import com.example.guera.DataProvisioner.Components.MessageBroker
import com.example.guera.DataProvisioner.Interfaces.IBoardService
import com.example.guera.DataProvisioner.Interfaces.IChecklistService
import com.example.guera.DataProvisioner.Interfaces.ITaskService
import com.example.guera.DataProvisioner.Models.Board
import com.example.guera.DataProvisioner.Models.Checklist
import com.example.guera.DataProvisioner.Models.Guerabook
import com.example.guera.DataProvisioner.Models.Task
import com.example.guera.DataProvisioner.Repositories.IBoardRepository
import com.example.guera.DataProvisioner.Repositories.IChecklistRepository
import com.example.guera.DataProvisioner.Repositories.IGuerabookRepository
import com.example.guera.DataProvisioner.Repositories.ITaskRepository
import org.hibernate.LazyInitializationException
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.beans.factory.getBean
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

    val boardService: IBoardService = context.getBean()
    val taskService: ITaskService = context.getBean()
    val checkService: IChecklistService = context.getBean()

    var checklistId = checkService.add(Checklist(title = "Basic Checklist", description = "I am the Senate", board = null))
    val task1 = Task(title = "Do the laundry", description = "", checklist = null)
    val task2 = Task(title = "Invoke magic", description = "", checklist = null)

    val task = taskService.find(taskService.add(task1, checklistId))
    taskService.add(task2, checklistId)


    val gotCheck = checkService.find(checklistId)
    gotCheck?.let {
        val tasks = it.tasksProxy()
        println(tasks[0].checklist)
    }
}
