package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Interfaces.IBoardService
import com.example.guera.DataProvisioner.Models.Board
import org.springframework.stereotype.Service

@Service("IBoardService")
class BoardService : IBoardService {

    override fun findBoard(id: Long): Board? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addBoard(board: Board): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addBoard(board: Board, gueraBookId: Long): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun modifyBoard(board: Board): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeBoard(id: Long): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}