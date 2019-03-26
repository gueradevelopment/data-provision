package com.example.guera.DataProvisioner.Controllers

import com.example.guera.DataProvisioner.Exceptions.BadRequestException
import com.example.guera.DataProvisioner.Exceptions.NotFoundException
import com.example.guera.DataProvisioner.Extensions.asJsonNode
import com.example.guera.DataProvisioner.Extensions.expectedProperties
import com.example.guera.DataProvisioner.Interfaces.IGuerabookController
import com.example.guera.DataProvisioner.Models.Guerabook
import com.example.guera.DataProvisioner.Models.Success
import com.example.guera.DataProvisioner.Services.GuerabookService
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class GuerabookController(
    @Autowired private val guerabookService: GuerabookService
) : IGuerabookController {


    override fun create(json: JsonNode): String {
        val guerabook = json.toGuerabook()
        val id = guerabookService.add(guerabook)
        return Success(id.asJsonNode("id")).toString()
    }

    override fun retrieve(json: JsonNode): String {
        if (!json.has("id")) throw BadRequestException(*Guerabook::class.expectedProperties("id"))
        val id = json["id"].textValue()
        val uuid = try { UUID.fromString(id) } catch (e: IllegalArgumentException) { throw BadRequestException("id") }
        val guerabook = guerabookService.find(uuid)
        guerabook ?: throw NotFoundException("Guerabook", id)
        return Success(guerabook).toString()
    }

    override fun retrieveAll(): String {
        val books = guerabookService.findAll()
        return Success(books).toString()
    }

    override fun retrieveAllId(): String {
        val idList = guerabookService.findAllId()
        return Success(idList).toString()
    }

    override fun delete(json: JsonNode): String {
        if (!json.has("id")) throw BadRequestException(*Guerabook::class.expectedProperties("id"))
        val id = json["id"].textValue()
        val uuid = try { UUID.fromString(id) } catch (e: IllegalArgumentException) { throw BadRequestException("id") }
        val success = guerabookService.remove(uuid)
        return if (success) Success(null).toString() else Error("Deletion Failure").toString()
    }

    override fun update(json: JsonNode): String {
        val guerabook = json.toGuerabook()
        guerabookService.modify(guerabook)
        return Success(null).toString()
    }

    private fun JsonNode.toGuerabook(): Guerabook {
        val book = try {
            ObjectMapper().treeToValue(this, Guerabook::class.java)!!
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
            val properties = Guerabook::class.expectedProperties("id")
            throw BadRequestException(*properties)
        }
        book.clearBoards()
        return book
    }
}