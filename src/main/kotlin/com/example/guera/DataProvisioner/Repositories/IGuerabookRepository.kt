package com.example.guera.DataProvisioner.Repositories

import com.example.guera.DataProvisioner.Models.Guerabook
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface IGuerabookRepository : MongoRepository<Guerabook, UUID>