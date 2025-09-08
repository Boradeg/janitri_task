package com.example.janitri_task.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VitalRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: VitalRecordEntity)

    @Query("SELECT * FROM vital_records ORDER BY id DESC")
    fun getAllVitals(): Flow<List<VitalRecordEntity>>
}
