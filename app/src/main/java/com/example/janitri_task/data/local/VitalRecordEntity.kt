package com.example.janitri_task.data.local

import androidx.room.*

@Entity(tableName = "vital_records")
data class VitalRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val heartRate: String,
    val bloodPressure: String,
    val weight: String,
    val kicks: String,
    val dateTime: Long
)




