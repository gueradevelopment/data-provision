package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Interfaces.IGuerabookService
import com.example.guera.DataProvisioner.Models.Guerabook
import com.example.guera.DataProvisioner.Repositories.IGuerabookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service("IGuerabookService")
class GuerabookService(
    @Autowired private val guerabookRepository: IGuerabookRepository
) : IGuerabookService {

    @Transactional
    override fun find(id: UUID): Guerabook? = guerabookRepository.findByIdOrNull(id)

    override fun add(element: Guerabook): UUID {
        val savedBook = guerabookRepository.save(element)
        println(savedBook)
        return savedBook.id
    }

    override fun findAll(): List<Guerabook> = guerabookRepository.findAll()

    override fun findAllId(): List<String> = guerabookRepository.findAll().map { it.id.toString() }

    override fun modify(element: Guerabook): Boolean {
        if (!guerabookRepository.existsById(element.id)) return false
        guerabookRepository.save(element)
        return true
    }

    override fun remove(id: UUID): Boolean {
        val exists = guerabookRepository.existsById(id)
        if (exists) guerabookRepository.deleteById(id)
        return exists
    }
}