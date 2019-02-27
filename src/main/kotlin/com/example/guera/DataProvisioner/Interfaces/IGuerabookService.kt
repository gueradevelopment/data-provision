package com.example.guera.DataProvisioner.Interfaces

import com.example.guera.DataProvisioner.Models.Guerabook

interface IGuerabookService {
    fun findGuerabook(id: Long): Guerabook?
    fun addGuerabook(guerabook: Guerabook): Long
    fun modifyGuerabook(guerabook: Guerabook): Boolean
    fun removeGuerabook(id: Long): Boolean
}