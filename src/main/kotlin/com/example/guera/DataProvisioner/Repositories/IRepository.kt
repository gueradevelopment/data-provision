package com.example.guera.DataProvisioner.Repositories

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

@NoRepositoryBean
interface IRepository<T> : MongoRepository<T, UUID>