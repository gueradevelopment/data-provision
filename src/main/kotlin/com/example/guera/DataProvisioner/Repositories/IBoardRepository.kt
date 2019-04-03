package com.example.guera.DataProvisioner.Repositories

import com.example.guera.DataProvisioner.Models.Board
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface IBoardRepository : MongoRepository<Board, UUID>