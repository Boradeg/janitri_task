package com.example.janitri_task.domain.model

data class VitalRecord(
    val heartRate: String,
    val bloodPressure: String,
    val weight: String,
    val kicks: String,
    val dateTime: Long = System.currentTimeMillis()
)

