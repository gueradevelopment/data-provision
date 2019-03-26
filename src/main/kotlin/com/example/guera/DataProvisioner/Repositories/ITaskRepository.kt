package com.example.guera.DataProvisioner.Repositories

import com.example.guera.DataProvisioner.Models.Task
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ITaskRepository : JpaRepository<Task, UUID>