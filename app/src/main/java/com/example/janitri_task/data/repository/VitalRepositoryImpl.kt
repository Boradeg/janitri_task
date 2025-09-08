package com.example.janitri_task.data.repository

import com.example.janitri_task.data.local.VitalRecordDao
import com.example.janitri_task.data.mapper.toDomain
import com.example.janitri_task.data.mapper.toEntity
import com.example.janitri_task.domain.repository.VitalRepository
import com.example.janitri_task.domain.model.VitalRecord
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VitalRepositoryImpl @Inject constructor(
    private val dao: VitalRecordDao
) : VitalRepository {

    override fun getAllVitals() = dao.getAllVitals().map { list ->
        list.map { entity -> entity.toDomain() }
    }

    override suspend fun insertVital(record: VitalRecord): Result<Unit> {
        return try {
            dao.insert(record.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
