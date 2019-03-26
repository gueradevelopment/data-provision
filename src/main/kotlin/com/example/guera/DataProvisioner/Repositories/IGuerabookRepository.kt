package com.example.guera.DataProvisioner.Repositories

import com.example.guera.DataProvisioner.Models.Checklist
import com.example.guera.DataProvisioner.Models.Guerabook
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface IGuerabookRepository : JpaRepository<Guerabook, UUID> {

    @Query("Select g From Guerabook g Left Join Fetch g.boards Where g.id = :id")
    override fun findById(@Param("id") id: UUID): Optional<Guerabook>

    @Query("Select g From Guerabook g Left Join Fetch g.boards Where g.id = :id")
    fun findByIdOrNull(@Param("id") id: UUID): Guerabook?

}