package com.example.guera.DataProvisioner.Interfaces

import com.example.guera.DataProvisioner.Models.Task
import java.util.*

interface ITaskService: ICrudService<Task> {
    fun add(task: Task, checklistId: UUID): UUID
    fun markAsComplete(id: UUID): Date?
}