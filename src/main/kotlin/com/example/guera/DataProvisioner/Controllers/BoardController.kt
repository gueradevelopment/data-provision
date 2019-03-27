package com.example.guera.DataProvisioner.Controllers

import com.example.guera.DataProvisioner.Exceptions.BadRequestException
import com.example.guera.DataProvisioner.Exceptions.NotFoundException
import com.example.guera.DataProvisioner.Extensions.asJsonNode
import com.example.guera.DataProvisioner.Extensions.expectedProperties
import com.example.guera.DataProvisioner.Extensions.toModel
import com.example.guera.DataProvisioner.Interfaces.IBoardController
import com.example.guera.DataProvisioner.Interfaces.IBoardService
import com.example.guera.DataProvisioner.Models.Board
import com.example.guera.DataProvisioner.Models.Failure
import com.example.guera.DataProvisioner.Models.Success
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import java.lang.IllegalArgumentException
import java.util.*

@Controller
class BoardController(
    @Autowired private val boardService: IBoardService
): IBoardController {

    override fun create(json: JsonNode): String {
        val board = json.toModel<Board>("title")
        val bookId = json["guerabookId"]
        val id = if (bookId != null) {
            val uuid = UUID.fromString(bookId.textValue())
            boardService.add(board, uuid)
        } else boardService.add(board)
        return Success(id.asJsonNode("id")).toString()
    }

    override fun retrieve(json: JsonNode): String {
        if (!json.has("id")) throw BadRequestException(*Board::class.expectedProperties("id"))
        val id = json["id"].textValue()
        val uuid = try { UUID.fromString(id) } catch (e: IllegalArgumentException) { throw BadRequestException("id") }
        val board = boardService.find(uuid)
        board ?: throw NotFoundException("Board", id)
        return Success(board).toString()
    }

    override fun retrieveAll(): String {
        val boards = boardService.findAll()
        return Success(boards).toString()
    }

    override fun retrieveAllId(): String {
        val idList = boardService.findAllId()
        return Success(idList).toString()
    }

    override fun delete(json: JsonNode): String {
        if (!json.has("id")) throw BadRequestException(*Board::class.expectedProperties("id"))
        val id = json["id"].textValue()
        val uuid = try { UUID.fromString(id) } catch (e: IllegalArgumentException) { throw BadRequestException("id") }
        val success = boardService.remove(uuid)
        return if (success) Success(null).toString() else Failure("Deletion Failure").toString()
    }

    override fun update(json: JsonNode): String {
        val board = json.toModel<Board>("id")
        boardService.modify(board)
        return Success(null).toString()
    }

}