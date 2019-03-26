package com.example.guera.DataProvisioner.Repositories

import com.example.guera.DataProvisioner.Models.Checklist
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface IChecklistRepository : JpaRepository<Checklist, UUID> {
    @Query("Select c From Checklist c Left Join Fetch c.tasks Where c.id = :id")
    override fun findById(@Param("id") id: UUID): Optional<Checklist>

    @Query("Select c From Checklist c Left Join Fetch c.tasks Where c.id = :id")
    fun findByIdOrNull(@Param("id") id: UUID): Checklist?
}