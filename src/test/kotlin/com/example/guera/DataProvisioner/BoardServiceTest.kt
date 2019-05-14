package com.example.guera.DataProvisioner

import com.example.guera.DataProvisioner.Interfaces.IBoardService
import com.example.guera.DataProvisioner.Interfaces.IChecklistService
import com.example.guera.DataProvisioner.Models.Board
import com.example.guera.DataProvisioner.Models.Checklist
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
class BoardServiceTest {

    companion object {

        lateinit var context: ConfigurableApplicationContext
        lateinit var boardService: IBoardService
        lateinit var checklistService: IChecklistService

        @BeforeClass @JvmStatic
        fun init() {
            context = runApplication<DataProvisionerApplication>()
            checklistService = context.getBean()
            boardService = context.getBean()
        }
    }

    @Test
    fun BoardBasicFlow() {
        val board = Board(title = "Senate", userId = "Generic", parentId = "")
        val id = boardService.add(board)
        val dbBoard = boardService.find(id)!!
        Assert.assertNotNull(dbBoard)
        val modBoard = dbBoard.copy(title = "Another title")
        val modSuccess = boardService.modify(modBoard)
        Assert.assertTrue(modSuccess)
        val delSuccess = boardService.remove(modBoard.id)
        Assert.assertTrue(delSuccess)
    }

    @Test
    fun BoardAddElementAndCascadeDelete() {
        val board = Board(title = "Senate", userId = "Generic", parentId = "")
        val id  = boardService.add(board)
        val checklistA = Checklist(title = "Checklist A", userId = "Generic", description = "", parentId = "")
        val checklistB = Checklist(title = "Checklist B", userId = "Generic", description = "", parentId = "")
        val idA = checklistService.add(checklistA, id)
        val idB = checklistService.add(checklistB, id)
        val boardIds = mutableListOf(idA, idB)
        val dbBoard = boardService.find(id)!!
        Assert.assertNotNull(dbBoard)
        var count = 0
        for (b in boardIds) {
            if (dbBoard.checklists.map { it.id }.contains(b)) count++
        }
        Assert.assertEquals(boardIds.size, count)
        val success = boardService.remove(id)
        Assert.assertTrue(success)
        Assert.assertEquals(0, checklistService.findAllId("Generic").size)
    }
}