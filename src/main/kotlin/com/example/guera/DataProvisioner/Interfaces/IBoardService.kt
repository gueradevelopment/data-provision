package com.example.guera.DataProvisioner.Interfaces

import com.example.guera.DataProvisioner.Models.Board
import java.util.*

interface IBoardService: ICrudService<Board> {
    fun add(board: Board, gueraBookId: UUID): UUID
}