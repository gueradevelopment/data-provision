package com.example.guera.DataProvisioner.Repositories

import com.example.guera.DataProvisioner.Models.Checklist
import org.springframework.data.repository.CrudRepository

interface IChecklistRepository : CrudRepository<Checklist, Long> {}