package com.example.guera.DataProvisioner.Services

import com.example.guera.DataProvisioner.Interfaces.IGuerabookService
import com.example.guera.DataProvisioner.Models.Guerabook
import com.example.guera.DataProvisioner.Repositories.IGuerabookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service("IGuerabookService")
class GuerabookService(
    @Autowired guerabookRepository: IGuerabookRepository,
    @Autowired private val boardService: BoardService
) : AbstractService<Guerabook>(guerabookRepository), IGuerabookService {

    override fun remove(id: UUID): Boolean {
        val success = super.remove(id)
        val childrenId = find(id)?.getBoardIds()
        val childSuccess = childrenId?.map { boardService.remove(UUID.fromString(it)) }
        return success && childSuccess?.fold(true) {prev, curr -> prev && curr} ?: true
    }

}