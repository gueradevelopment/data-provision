package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Interfaces.IGuerabookService
import com.example.guera.DataProvisioner.Models.Guerabook
import org.springframework.stereotype.Service

@Service("IGuerabookService")
class GuerabookService : IGuerabookService {

    override fun findGuerabook(id: Long): Guerabook? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addGuerabook(guerabook: Guerabook): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun modifyGuerabook(guerabook: Guerabook): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeGuerabook(id: Long): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}