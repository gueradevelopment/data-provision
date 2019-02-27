package com.example.guera.DataProvisioner.Interfaces

import com.example.guera.DataProvisioner.Models.Board

interface IBoardService {
    fun findBoard(id: Long): Board?
    fun addBoard(board: Board): Long
    fun addBoard(board: Board, gueraBookId: Long): Long
    fun modifyBoard(board: Board): Boolean
    fun removeBoard(id: Long): Boolean
}