package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Extensions.unwrap
import com.example.guera.DataProvisioner.Interfaces.IGuerabookService
import com.example.guera.DataProvisioner.Models.Guerabook
import com.example.guera.DataProvisioner.Repositories.IGuerabookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service("IGuerabookService")
class GuerabookService(
    @Autowired guerabookRepository: IGuerabookRepository
) : AbstractService<Guerabook>(guerabookRepository), IGuerabookService