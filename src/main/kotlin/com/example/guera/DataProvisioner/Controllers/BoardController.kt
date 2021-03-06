package com.example.guera.DataProvisioner.Controllers

import com.example.guera.DataProvisioner.Exceptions.BadRequestException
import com.example.guera.DataProvisioner.Exceptions.NotFoundException
import com.example.guera.DataProvisioner.Extensions.asJsonNode
import com.example.guera.DataProvisioner.Extensions.expectedProperties
import com.example.guera.DataProvisioner.Extensions.toModel
import com.example.guera.DataProvisioner.Extensions.toReq
import com.example.guera.DataProvisioner.Interfaces.IBoardController
import com.example.guera.DataProvisioner.Interfaces.IBoardService
import com.example.guera.DataProvisioner.Models.Board
import com.example.guera.DataProvisioner.Models.BoardReq
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
        val board = json.toModel<Board>("title", "userId", "guerabookId")
        val bookId = json["guerabookId"]
        val id = if (bookId != null) {
            val uuid = UUID.fromString(bookId.textValue())
            boardService.add(board.copy(parentId = uuid.toString()), uuid)
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

    override fun retrieveAll(userId: String, isTeamContext: Boolean): String {
        val boards = boardService.findAll(userId, isTeamContext)
        return Success(boards).toString()
    }

    override fun retrieveAllId(userId: String, isTeamContext: Boolean): String {
        val idList = boardService.findAllId(userId, isTeamContext)
        return Success(idList).toString()
    }

    override fun delete(json: JsonNode): String {
        if (!json.has("id")) throw BadRequestException(*Board::class.expectedProperties("id"))
        val id = json["id"].textValue()
        val uuid = try { UUID.fromString(id) } catch (e: IllegalArgumentException) { throw BadRequestException("id") }
        val success = boardService.remove(uuid)
        return if (success) Success(null).toString() else Failure("Deletion Failure", "InternalServerError").toString()
    }

    override fun update(json: JsonNode): String {
        val request = json.toReq<BoardReq>()
        val board = boardService.find(request.id) ?: throw NotFoundException("Board", request.id.toString())
        val query = board.copy(
            title = request.title ?: board.title
        )
        val success = boardService.modify(query)
        return if (success) Success(null).toString() else throw NotFoundException("Board", board.id.toString())
    }

}