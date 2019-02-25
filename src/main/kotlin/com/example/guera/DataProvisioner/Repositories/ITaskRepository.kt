package com.example.guera.DataProvisioner.Repositories

import com.example.guera.DataProvisioner.Models.Task
import org.springframework.data.repository.CrudRepository

interface ITaskRepository : CrudRepository<Task, Long> {}