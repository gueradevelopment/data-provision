package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Interfaces.IGuerabookService
import com.example.guera.DataProvisioner.Models.Guerabook
import com.example.guera.DataProvisioner.Repositories.IGuerabookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service("IGuerabookService")
class GuerabookService(
    @Autowired private val guerabookRepository: IGuerabookRepository
) : IGuerabookService {

    override fun findGuerabook(id: Long): Guerabook? = guerabookRepository.findByIdOrNull(id)

    override fun addGuerabook(guerabook: Guerabook): Long {
        val savedBook = guerabookRepository.save(guerabook)
        return savedBook.id
    }

    override fun modifyGuerabook(guerabook: Guerabook): Boolean {
        if (!guerabookRepository.existsById(guerabook.id)) return false
        guerabookRepository.save(guerabook)
        return true
    }

    override fun removeGuerabook(id: Long): Boolean {
        val exists = guerabookRepository.existsById(id)
        guerabookRepository.deleteById(id)
        return exists
    }
}