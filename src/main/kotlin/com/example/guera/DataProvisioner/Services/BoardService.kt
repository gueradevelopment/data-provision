package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Extensions.unwrap
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
    @Autowired boardRepository: IBoardRepository,
    @Autowired private val guerabookRepository: IGuerabookRepository
) : AbstractService<Board>(boardRepository), IBoardService {

    override fun add(board: Board, gueraBookId: UUID): UUID {
        val id = add(board)
        val gueraBook = guerabookRepository.findById(gueraBookId).unwrap() ?: return UUID(0, 0)
        gueraBook.boards.add(board)
        guerabookRepository.save(gueraBook)
        return id
    }
}