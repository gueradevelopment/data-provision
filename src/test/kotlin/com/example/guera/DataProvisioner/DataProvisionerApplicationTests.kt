package com.example.guera.DataProvisioner


import com.example.guera.DataProvisioner.Interfaces.IGuerabookService
import com.example.guera.DataProvisioner.Interfaces.ITaskService
import com.example.guera.DataProvisioner.Models.Board
import com.example.guera.DataProvisioner.Models.Checklist
import com.example.guera.DataProvisioner.Models.Guerabook
import com.example.guera.DataProvisioner.Models.Task
import com.example.guera.DataProvisioner.Repositories.IBoardRepository
import com.example.guera.DataProvisioner.Repositories.IChecklistRepository
import com.example.guera.DataProvisioner.Repositories.IGuerabookRepository
import com.example.guera.DataProvisioner.Repositories.ITaskRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.getBean
import org.springframework.boot.runApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class DataProvisionerApplicationTests {

	companion object {

		lateinit var context: ConfigurableApplicationContext
		lateinit var gueraBookRepo: IGuerabookRepository
		lateinit var boardRepo: IBoardRepository
		lateinit var checkRepo: IChecklistRepository
		lateinit var taskRepo: ITaskRepository

		lateinit var taskService: ITaskService
		lateinit var guerabookService: IGuerabookService
		lateinit var theId: UUID

		@BeforeClass @JvmStatic
		fun init() {
			context = runApplication<DataProvisionerApplication>()
			gueraBookRepo = context.getBean()
			boardRepo = context.getBean()
			checkRepo = context.getBean()
			taskRepo = context.getBean()
			taskService = context.getBean()
			guerabookService = context.getBean()
		}

	}

	@Test
	fun basicStoring() {

		val userId = UUID.randomUUID().toString()

		val book = gueraBookRepo.save(Guerabook(userId = userId, title = "First book"))
		val board = boardRepo.save(Board(userId = userId, title = "First Board"))
		var checklist = checkRepo.save(Checklist(userId = userId, title = "Basic Checklist", description = "I am the Senate"))
		val task1 = Task(userId = userId, title = "Do the laundry", description = "")
		val task2 = Task(userId = userId, title = "Invoke magic", description = "")

		val id = taskRepo.save(task1).id
		theId = book.id

		taskRepo.save(task2)

		val task = taskService.find(id) ?: throw Exception("Null lol")

		//Assert.assertEquals("I am the Senate", task.checklist?.description)
		Assert.assertEquals("Do the laundry", task.title)

		val gotCheck = checkRepo.findById(checklist.id).get()
		val tasks = gotCheck.tasks
		println(tasks.size)
	}

	@Test
	fun basicRetrieval() {

		val book = guerabookService.find(theId)
		Assert.assertNotNull(book)
		val str = ObjectMapper().writeValueAsString(book)
		println(str)
	}

}
