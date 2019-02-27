package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Interfaces.ITaskService
import com.example.guera.DataProvisioner.Models.Task
import com.example.guera.DataProvisioner.Repositories.IChecklistRepository
import com.example.guera.DataProvisioner.Repositories.ITaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.Date
import java.time.Instant

@Service("ITaskService")
class TaskService(
    @Autowired private val taskRepository: ITaskRepository,
    @Autowired private val checklistRepository: IChecklistRepository,
    @Autowired private val boardRepository: IChecklistRepository
) : ITaskService {

    override fun findTask(id: Long): Task? = taskRepository.findByIdOrNull(id)

    override fun addTask(task: Task): Long {
        val savedTask = taskRepository.save(task)
        return savedTask.id
    }

    override fun addTask(task: Task, checklistId: Long): Long {
        val checklist = checklistRepository.findByIdOrNull(checklistId) ?: return 0L
        val board = checklist.board ?: return 0L
        task.board = board
        task.checklist = checklist
        return addTask(task)
    }

    override fun modifyTask(task: Task): Boolean {
        if (!taskRepository.existsById(task.id)) return false
        taskRepository.save(task)
        return true
    }

    override fun removeTask(id: Long): Boolean {
        val exists = taskRepository.existsById(id)
        taskRepository.deleteById(id)
        return exists
    }

    override fun markAsComplete(id: Long): Date? {
        val task = taskRepository.findByIdOrNull(id) ?: return null
        if (task.completionDate != null) return null
        task.completionDate = Date.from(Instant.now())
        taskRepository.save(task)
        return task.completionDate
    }
}