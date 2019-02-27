package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Interfaces.IBoardService
import com.example.guera.DataProvisioner.Models.Board
import com.example.guera.DataProvisioner.Repositories.IBoardRepository
import com.example.guera.DataProvisioner.Repositories.IGuerabookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service("IBoardService")
class BoardService(
    @Autowired private val boardRepository: IBoardRepository,
    @Autowired private val guerabookRepository: IGuerabookRepository
) : IBoardService {

    override fun findBoard(id: Long): Board? = boardRepository.findByIdOrNull(id)

    override fun addBoard(board: Board): Long {
        val savedBoard = boardRepository.save(board)
        return savedBoard.id
    }

    override fun addBoard(board: Board, gueraBookId: Long): Long {
        val gueraBook = guerabookRepository.findByIdOrNull(gueraBookId) ?: return 0L
        board.guerabook = gueraBook
        return addBoard(board)
    }

    override fun modifyBoard(board: Board): Boolean {
        if (!boardRepository.existsById(board.id)) return false
        boardRepository.save(board)
        return true
    }

    override fun removeBoard(id: Long): Boolean {
        val exists = boardRepository.existsById(id)
        boardRepository.deleteById(id)
        return exists
    }
}