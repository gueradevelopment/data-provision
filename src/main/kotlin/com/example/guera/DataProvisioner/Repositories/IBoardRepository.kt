package com.example.guera.DataProvisioner.Repositories

import com.example.guera.DataProvisioner.Models.Board
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface IBoardRepository : JpaRepository<Board, UUID> {

    @Query("Select b From Board b Left Join Fetch b.checklists Where b.id = :id")
    override fun findById(@Param("id") id: UUID): Optional<Board>

    @Query("Select b From Board b Left Join Fetch b.checklists Where b.id = :id")
    fun findByIdOrNull(@Param("id") id: UUID): Board?

}