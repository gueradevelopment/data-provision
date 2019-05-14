package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Exceptions.NotFoundException
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
import kotlin.reflect.jvm.internal.impl.util.Check

@Service("IChecklistService")
class ChecklistService(
    @Autowired checklistRepository: IChecklistRepository,
    @Autowired private val boardRepository: IBoardRepository,
    @Autowired private val taskService: ITaskService
) : AbstractService<Checklist>(checklistRepository), IChecklistService {


    override fun add(checklist: Checklist, boardId: UUID): UUID {
        val board = boardRepository.findById(boardId).unwrap() ?: throw NotFoundException("Board", boardId.toString())
        val id = add(checklist)
        board.checklists.add(checklist)
        boardRepository.save(board)
        return id
    }

    override fun remove(id: UUID): Boolean {
        val childrenId = find(id)?.getTaskIds()
        val childSuccess = childrenId?.map { taskService.remove(UUID.fromString(it)) }
        val success = super.remove(id)
        return success && childSuccess?.fold(true) {prev, curr -> prev && curr} ?: true
    }

    override fun markAsComplete(id: UUID): Date? {
        val checklist = find(id) ?: return null
        if (checklist.completionDate != null) return null
        val now = Date.from(Instant.now())
        checklist.completionDate = now
        modify(checklist)
        checklist.tasks.forEach { taskService.markAsComplete(it.id) }
        return now
    }
}