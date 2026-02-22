package com.example.blindguard

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class AlertRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val type: String,
    val latitude: String,
    val longitude: String,
    val timestamp: Long
)