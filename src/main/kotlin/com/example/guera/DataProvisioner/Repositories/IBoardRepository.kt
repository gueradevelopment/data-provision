package com.example.guera.DataProvisioner.Repositories

import com.example.guera.DataProvisioner.Models.Board
import org.springframework.data.repository.CrudRepository

interface IBoardRepository : CrudRepository<Board, Long> {}