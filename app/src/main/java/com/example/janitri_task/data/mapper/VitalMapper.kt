package com.example.janitri_task.data.mapper

import com.example.janitri_task.data.local.VitalRecordEntity
import com.example.janitri_task.domain.model.VitalRecord

fun VitalRecordEntity.toDomain(): VitalRecord {
    return VitalRecord(
        heartRate = heartRate,
        bloodPressure = bloodPressure,
        weight = weight,
        kicks = kicks,
        dateTime = dateTime
    )
}

fun VitalRecord.toEntity(): VitalRecordEntity {
    return VitalRecordEntity(
        heartRate = heartRate,
        bloodPressure = bloodPressure,
        weight = weight,
        kicks = kicks,
        dateTime = dateTime
    )
}