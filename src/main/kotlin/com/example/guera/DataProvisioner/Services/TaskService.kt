package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Interfaces.ITaskService
import com.example.guera.DataProvisioner.Models.Task
import com.example.guera.DataProvisioner.Repositories.IChecklistRepository
import com.example.guera.DataProvisioner.Repositories.ITaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service("ITaskService")
class TaskService(
    @Autowired private val taskRepository: ITaskRepository,
    @Autowired private val checklistRepository: IChecklistRepository
) : ITaskService {

    @Transactional
    override fun find(id: UUID): Task? = taskRepository.findByIdOrNull(id)

    override fun add(element: Task): UUID {
        val savedTask = taskRepository.save(element)
        return savedTask.id
    }

    override fun add(task: Task, checklistId: UUID): UUID {
        val checklist = checklistRepository.findByIdOrNull(checklistId) ?: return UUID(0, 0)
        task.checklist = checklist
        return add(task)


    }

    override fun findAll(): List<Task> = taskRepository.findAll()

    override fun findAllId(): List<String> = taskRepository.findAll().map { it.id.toString() }

    override fun modify(element: Task): Boolean {
        if (!taskRepository.existsById(element.id)) return false
        taskRepository.save(element)
        return true
    }

    override fun remove(id: UUID): Boolean {
        val exists = taskRepository.existsById(id)
        taskRepository.deleteById(id)
        return exists
    }

    override fun markAsComplete(id: UUID): Date? {
        val task = taskRepository.findByIdOrNull(id) ?: return null
        if (task.completionDate != null) return null
        task.completionDate = Date.from(Instant.now())
        taskRepository.save(task)
        return task.completionDate
    }
}