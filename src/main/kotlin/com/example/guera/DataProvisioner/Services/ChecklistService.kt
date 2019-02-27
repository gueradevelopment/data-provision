package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Interfaces.IChecklistService
import com.example.guera.DataProvisioner.Models.Checklist
import com.example.guera.DataProvisioner.Repositories.IBoardRepository
import com.example.guera.DataProvisioner.Repositories.IChecklistRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service("IChecklistService")
class ChecklistService(
    @Autowired private val checklistRepository: IChecklistRepository,
    @Autowired private val boardRepository: IBoardRepository
) : IChecklistService {

    override fun findChecklist(id: Long): Checklist? = checklistRepository.findByIdOrNull(id)

    override fun addChecklist(checklist: Checklist): Long {
        val savedChecklist = checklistRepository.save(checklist)
        return savedChecklist.id
    }

    override fun addChecklist(checklist: Checklist, boardId: Long): Long {
        val board = boardRepository.findByIdOrNull(boardId) ?: return 0L
        checklist.board = board
        return addChecklist(checklist)
    }

    override fun modifyChecklist(checklist: Checklist): Boolean {
        if (!checklistRepository.existsById(checklist.id)) return false
        checklistRepository.save(checklist)
        return true
    }

    override fun removeChecklist(id: Long): Boolean {
        val exists = checklistRepository.existsById(id)
        checklistRepository.deleteById(id)
        return exists
    }

    override fun markAsComplete(id: Long): Date? {
        val checklist = checklistRepository.findByIdOrNull(id) ?: return null
        if (checklist.completionDate != null) return null
        val now = Date.from(Instant.now())
        checklist.completionDate = now
        checklist.tasks.forEach { if (it.completionDate != null) it.completionDate = now }
        return now
    }
}