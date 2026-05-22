package com.nammaraste.health.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val road: String,
    val type: String,
    val severity: String,
    val date: String,
    val name: String,
    val gps: String,
    val imageUri: String? = null
)
