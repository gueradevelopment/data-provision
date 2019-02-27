package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Interfaces.IChecklistService
import com.example.guera.DataProvisioner.Models.Checklist
import org.springframework.stereotype.Service
import java.util.*

@Service("IChecklistService")
class ChecklistService : IChecklistService {

    override fun findChecklist(id: Long): Checklist? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addChecklist(checklist: Checklist): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addChecklist(checklist: Checklist, boardId: Long): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun modifyChecklist(checklist: Checklist): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeChecklist(id: Long): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun markAsComplete(id: Long): Date? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}