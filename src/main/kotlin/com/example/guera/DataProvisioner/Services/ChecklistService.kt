package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Extensions.unwrap
import com.example.guera.DataProvisioner.Interfaces.IChecklistService
import com.example.guera.DataProvisioner.Interfaces.ITaskService
import com.example.guera.DataProvisioner.Models.Checklist
import com.example.guera.DataProvisioner.Repositories.IBoardRepository
import com.example.guera.DataProvisioner.Repositories.IChecklistRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service("IChecklistService")
class ChecklistService(
    @Autowired private val checklistRepository: IChecklistRepository,
    @Autowired private val boardRepository: IBoardRepository,
    @Autowired private val taskService: ITaskService
) : IChecklistService {

    @Transactional
    override fun find(id: UUID): Checklist? = checklistRepository.findById(id).orElse(null)

    override fun add(checklist: Checklist): UUID {
        val savedChecklist = checklistRepository.save(checklist)
        return savedChecklist.id
    }

    override fun add(checklist: Checklist, boardId: UUID): UUID {
        val board = boardRepository.findById(boardId).unwrap() ?: return UUID(0, 0)
        checklist.board = board
        return add(checklist)
    }

    @Transactional
    override fun findAll(): List<Checklist> = checklistRepository.findAll()

    @Transactional
    override fun findAllId(): List<String> = checklistRepository.findAll().map { it.id.toString() }

    override fun modify(checklist: Checklist): Boolean {
        if (!checklistRepository.existsById(checklist.id)) return false
        checklistRepository.save(checklist)
        return true
    }

    override fun remove(id: UUID): Boolean {
        val exists = checklistRepository.existsById(id)
        if (exists) checklistRepository.deleteById(id)
        return exists
    }

    override fun markAsComplete(id: UUID): Date? {
        val checklist = checklistRepository.findById(id).unwrap() ?: return null
        if (checklist.completionDate != null) return null
        val now = Date.from(Instant.now())
        checklist.completionDate = now
        modify(checklist)
        checklist.tasks.forEach { taskService.markAsComplete(it.id) }
        return now
    }
}