package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Exceptions.NotFoundException
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
    @Autowired taskRepository: ITaskRepository,
    @Autowired private val checklistRepository: IChecklistRepository
) : AbstractService<Task>(taskRepository), ITaskService {

    override fun add(task: Task, checklistId: UUID): UUID {
        val checklist = checklistRepository.findByIdOrNull(checklistId) ?: throw NotFoundException("Checklist", checklistId.toString())
        val id = add(task)
        checklist.tasks.add(task)
        checklistRepository.save(checklist)
        return id
    }

    override fun markAsComplete(id: UUID): Date? {
        val task = find(id) ?: return null
        task.completionDate = Date.from(Instant.now())
        modify(task)
        return task.completionDate
    }
}