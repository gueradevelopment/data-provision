package com.example.guera.DataProvisioner.Interfaces

import com.example.guera.DataProvisioner.Models.Task
import java.util.*

interface ITaskService {
    fun findTask(id: Long): Task?
    fun addTask(task: Task): Long
    fun addTask(task: Task, checklistId: Long): Long
    fun modifyTask(task: Task): Boolean
    fun removeTask(id: Long): Boolean
    fun markAsComplete(id: Long): Date?
}