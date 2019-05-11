package com.example.guera.DataProvisioner.Interfaces

import java.util.*

interface ICrudService<T> {
    fun find(id: UUID): T?
    fun findAll(userId: String): List<T>
    fun findAllId(userId: String): List<String>
    fun add(element: T): UUID
    fun modify(element: T): Boolean
    fun remove(id: UUID): Boolean
}