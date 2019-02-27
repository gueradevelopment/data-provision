package com.example.guera.DataProvisioner.Interfaces

import com.example.guera.DataProvisioner.Models.Checklist
import java.util.*

interface IChecklistService  {
    fun findChecklist(id: Long): Checklist?
    fun addChecklist(checklist: Checklist): Long
    fun addChecklist(checklist: Checklist, boardId: Long): Long
    fun modifyChecklist(checklist: Checklist): Boolean
    fun removeChecklist(id: Long): Boolean
    fun markAsComplete(id: Long): Date?
}