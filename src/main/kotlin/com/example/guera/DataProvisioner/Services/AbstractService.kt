package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Interfaces.ICrudService
import com.example.guera.DataProvisioner.Models.Identified
import com.example.guera.DataProvisioner.Repositories.IRepository
import java.util.*

abstract class AbstractService<T : Identified>(
    private val repository: IRepository<T>
) : ICrudService<T> {

    override fun find(id: UUID): T? = repository.findById(id).orElse(null)

    override fun findAll(): List<T> = repository.findAll()

    override fun findAllId(): List<String> = findAll().map { it.id.toString() }

    override fun add(element: T): UUID {
        val savedElement = repository.save(element)
        return savedElement.id
    }

    override fun modify(element: T): Boolean {
        if (!repository.existsById(element.id)) return false
        repository.save(element)
        return true
    }

    override fun remove(id: UUID): Boolean {
        val exists = repository.existsById(id)
        if (exists) repository.deleteById(id)
        return exists
    }
}