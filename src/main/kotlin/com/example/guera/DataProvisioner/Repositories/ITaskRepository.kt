package com.example.guera.DataProvisioner.Repositories

import com.example.guera.DataProvisioner.Models.Task
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ITaskRepository : MongoRepository<Task, UUID>