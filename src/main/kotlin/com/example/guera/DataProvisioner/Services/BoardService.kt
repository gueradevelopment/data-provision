package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Interfaces.IBoardService
import com.example.guera.DataProvisioner.Models.Board
import com.example.guera.DataProvisioner.Repositories.IBoardRepository
import com.example.guera.DataProvisioner.Repositories.IGuerabookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service("IBoardService")
class BoardService(
    @Autowired private val boardRepository: IBoardRepository,
    @Autowired private val guerabookRepository: IGuerabookRepository
) : IBoardService {

    @Transactional
    override fun find(id: UUID): Board? = boardRepository.findByIdOrNull(id)

    override fun add(element: Board): UUID {
        val savedBoard = boardRepository.save(element)
        return savedBoard.id
    }

    override fun add(board: Board, gueraBookId: UUID): UUID {
        val gueraBook = guerabookRepository.findByIdOrNull(gueraBookId) ?: return UUID(0, 0)
        board.guerabook = gueraBook
        return add(board)
    }

    @Transactional
    override fun findAll(): List<Board> = boardRepository.findAll()

    @Transactional
    override fun findAllId(): List<String> = boardRepository.findAll().map { it.id.toString() }

    override fun modify(element: Board): Boolean {
        if (!boardRepository.existsById(element.id)) return false
        boardRepository.save(element)
        return true
    }

    override fun remove(id: UUID): Boolean {
        val exists = boardRepository.existsById(id)
        boardRepository.deleteById(id)
        return exists
    }
}