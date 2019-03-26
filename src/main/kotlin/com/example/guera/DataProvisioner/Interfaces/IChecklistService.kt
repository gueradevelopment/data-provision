package com.example.guera.DataProvisioner.Interfaces

import com.example.guera.DataProvisioner.Models.Checklist
import java.util.*

interface IChecklistService: ICrudService<Checklist> {
    fun add(checklist: Checklist, boardId: UUID): UUID
    fun markAsComplete(id: UUID): Date?
}