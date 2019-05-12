package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Interfaces.IGuerateamService
import com.example.guera.DataProvisioner.Models.Guerateam
import com.example.guera.DataProvisioner.Repositories.IGuerateamRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("IGuerateamService")
class GuerateamService(
    @Autowired private val guerateamRepository: IGuerateamRepository
) : AbstractService<Guerateam>(guerateamRepository), IGuerateamService {

    override fun findAll(userId: String, isTeamContext: Boolean): List<Guerateam> = guerateamRepository.findAll()
        .filter { userId == it.ownerId || userId in it.membersId }
        .filter { it.isTeamContext == isTeamContext }

}