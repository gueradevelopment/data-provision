package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Exceptions.NotFoundException
import com.example.guera.DataProvisioner.Extensions.unwrap
import com.example.guera.DataProvisioner.Interfaces.IBoardService
import com.example.guera.DataProvisioner.Interfaces.IChecklistService
import com.example.guera.DataProvisioner.Models.Board
import com.example.guera.DataProvisioner.Repositories.IBoardRepository
import com.example.guera.DataProvisioner.Repositories.IGuerabookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception
import java.util.*

@Service("IBoardService")
class BoardService(
    @Autowired boardRepository: IBoardRepository,
    @Autowired private val guerabookRepository: IGuerabookRepository,
    @Autowired private val checklistService: IChecklistService
) : AbstractService<Board>(boardRepository), IBoardService {

    override fun add(board: Board, gueraBookId: UUID): UUID {
        val id = add(board)
        val gueraBook = guerabookRepository.findById(gueraBookId).unwrap() ?: throw NotFoundException("Guerabook", gueraBookId.toString())
        gueraBook.boards.add(board)
        guerabookRepository.save(gueraBook)
        return id
    }

    override fun remove(id: UUID): Boolean {
        val board  = find(id) ?: throw NotFoundException("Board", id.toString())
        val childrenId = board.getChecklistIds()
        val childSuccess = childrenId.map { checklistService.remove(UUID.fromString(it)) }
        val parent = guerabookRepository.findById(UUID.fromString(board.parentId)).unwrap()
        val newBoards = parent?.boards?.filter { it.id != id }?.toMutableSet()
        val newParent = parent?.copy(boards = newBoards!!) ?: throw Exception("Rip")
        guerabookRepository.save(newParent)
        val success = super.remove(id)
        return success  &&  childSuccess.fold(true) {prev, curr -> prev && curr} ?: true
    }
}