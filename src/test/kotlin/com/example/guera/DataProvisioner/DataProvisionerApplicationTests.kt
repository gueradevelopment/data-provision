package com.example.guera.DataProvisioner

import com.example.guera.DataProvisioner.Models.Board
import com.example.guera.DataProvisioner.Models.Checklist
import com.example.guera.DataProvisioner.Models.Guerabook
import com.example.guera.DataProvisioner.Models.Task
import com.example.guera.DataProvisioner.Repositories.IBoardRepository
import com.example.guera.DataProvisioner.Repositories.IChecklistRepository
import com.example.guera.DataProvisioner.Repositories.IGuerabookRepository
import com.example.guera.DataProvisioner.Repositories.ITaskRepository

import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.getBean
import org.springframework.boot.runApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class DataProvisionerApplicationTests {

	companion object {

		lateinit var context: ConfigurableApplicationContext
		lateinit var gueraBookRepo: IGuerabookRepository
		lateinit var boardRepo: IBoardRepository
		lateinit var checkRepo: IChecklistRepository
		lateinit var taskRepo: ITaskRepository

		@BeforeClass @JvmStatic
		fun init() {
			context = runApplication<DataProvisionerApplication>()
			gueraBookRepo = context.getBean()
			boardRepo = context.getBean()
			checkRepo = context.getBean()
			taskRepo = context.getBean()
		}

	}

	@Test
	fun basicStoring() {
		val book = gueraBookRepo.save(Guerabook(title = "First book"))
		val board = boardRepo.save(Board(title = "First Board", guerabook = book))
		val checklist = checkRepo.save(Checklist(title = "Basic Checklist", description = "I am the Senate", board = board))
		val task1 = Task(title = "Do the laundry", description = "", checklist = checklist, board = board, completionDate = null)

		val task = taskRepo.save(task1)

		Assert.assertEquals("First book", task.board?.guerabook?.title)
		Assert.assertEquals("Do the laundry", task.title)
	}

}
