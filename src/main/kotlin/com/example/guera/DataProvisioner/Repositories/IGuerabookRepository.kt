package com.example.guera.DataProvisioner.Repositories

import com.example.guera.DataProvisioner.Models.Guerabook
import org.springframework.data.repository.CrudRepository

interface IGuerabookRepository : CrudRepository<Guerabook, Long> {}