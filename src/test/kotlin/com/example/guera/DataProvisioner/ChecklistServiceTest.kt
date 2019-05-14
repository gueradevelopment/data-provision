package com.example.guera.DataProvisioner

import com.example.guera.DataProvisioner.Interfaces.IChecklistService
import com.example.guera.DataProvisioner.Interfaces.ITaskService
import com.example.guera.DataProvisioner.Models.Checklist
import com.example.guera.DataProvisioner.Models.Task
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
class ChecklistServiceTest {

    companion object {

        lateinit var context: ConfigurableApplicationContext
        lateinit var taskService: ITaskService
        lateinit var checklistService: IChecklistService

        @BeforeClass @JvmStatic
        fun init() {
            context = runApplication<DataProvisionerApplication>()
            checklistService = context.getBean()
            taskService = context.getBean()
        }
    }

    @Test
    fun ChecklistBasicFlow() {
        val checklist = Checklist(title = "Senate", userId = "Generic", description = "")
        val id = checklistService.add(checklist)
        val dbChecklist = checklistService.find(id)!!
        Assert.assertNotNull(dbChecklist)
        val modBoard = dbChecklist.copy(title = "Another title")
        val modSuccess = checklistService.modify(modBoard)
        Assert.assertTrue(modSuccess)
        val delSuccess = checklistService.remove(modBoard.id)
        Assert.assertTrue(delSuccess)
    }

    @Test
    fun ChecklistAddElementAndCascadeDelete() {
        val checklist = Checklist(title = "Senate", userId = "Generic", description = "")
        val id  = checklistService.add(checklist)
        val taskA = Task(title = "Task A", userId = "Generic", description = "")
        val taskB = Task(title = "Task B", userId = "Generic", description = "")
        val idA = taskService.add(taskA, id)
        val idB = taskService.add(taskB, id)
        val boardIds = mutableListOf(idA, idB)
        val dbChecklist = checklistService.find(id)!!
        Assert.assertNotNull(dbChecklist)
        var count = 0
        for (b in boardIds) {
            if (dbChecklist.tasks.map { it.id }.contains(b)) count++
        }
        Assert.assertEquals(boardIds.size, count)
        val success = checklistService.remove(id)
        Assert.assertTrue(success)
        Assert.assertEquals(0, taskService.findAllId("Generic").size)
    }
}