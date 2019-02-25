package com.example.guera.DataProvisioner

import com.example.guera.DataProvisioner.Models.Board
import com.example.guera.DataProvisioner.Models.Checklist
import com.example.guera.DataProvisioner.Models.Guerabook
import com.example.guera.DataProvisioner.Models.Task
import com.example.guera.DataProvisioner.Repositories.IBoardRepository
import com.example.guera.DataProvisioner.Repositories.IChecklistRepository
import com.example.guera.DataProvisioner.Repositories.IGuerabookRepository
import com.example.guera.DataProvisioner.Repositories.ITaskRepository
import org.slf4j.LoggerFactory

import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DataProvisionerApplication

fun main(args: Array<String>) {
    val context = runApplication<DataProvisionerApplication>(*args)
}
