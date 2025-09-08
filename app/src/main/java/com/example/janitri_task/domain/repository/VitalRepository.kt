package com.example.janitri_task.domain.repository


import com.example.janitri_task.domain.model.VitalRecord
import kotlinx.coroutines.flow.Flow

interface VitalRepository {
    fun getAllVitals(): Flow<List<VitalRecord>>
    suspend fun insertVital(record: VitalRecord): Result<Unit>
}
