package com.example.guera.DataProvisioner

import com.example.guera.DataProvisioner.Interfaces.IBoardService
import com.example.guera.DataProvisioner.Interfaces.IGuerabookService
import com.example.guera.DataProvisioner.Models.Board
import com.example.guera.DataProvisioner.Models.Guerabook
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
class BookServiceTest {

    companion object {

        lateinit var context: ConfigurableApplicationContext
        lateinit var guerabookService: IGuerabookService
        lateinit var boardService: IBoardService

        @BeforeClass @JvmStatic
        fun init() {
            context = runApplication<DataProvisionerApplication>()
            guerabookService = context.getBean()
            boardService = context.getBean()
        }
    }

    @Test
    fun BookbasicFlow() {
        val book = Guerabook(title = "Senate", userId = "Generic")
        val id = guerabookService.add(book)
        val dbBook = guerabookService.find(id)!!
        Assert.assertNotNull(dbBook)
        val modBook = dbBook.copy(title = "Another title")
        val modSuccess = guerabookService.modify(modBook)
        Assert.assertTrue(modSuccess)
        val delSuccess = guerabookService.remove(modBook.id)
        Assert.assertTrue(delSuccess)
    }

    @Test
    fun BookaddElementAndCascadeDelete() {
        val book = Guerabook(title = "Senate", userId = "Generic")
        val id  = guerabookService.add(book)
        val boardA = Board(title = "Board A", userId = "Generic", parentId = "")
        val boardB = Board(title = "Board B", userId = "Generic", parentId = "")
        val idA = boardService.add(boardA, id)
        val idB = boardService.add(boardB, id)
        val boardIds = mutableListOf(idA, idB)
        val dbBook = guerabookService.find(id)!!
        Assert.assertNotNull(dbBook)
        var count = 0
        for (b in boardIds) {
            if (dbBook.boards.map { it.id }.contains(b)) count++
        }
        Assert.assertEquals(boardIds.size, count)
        val success = guerabookService.remove(id)
        Assert.assertTrue(success)
        Assert.assertEquals(0, boardService.findAllId("Generic").size)
    }
}