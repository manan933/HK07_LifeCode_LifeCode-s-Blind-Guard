package com.example.blindguard

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alert: AlertRecord)

    @Query("SELECT * FROM alerts ORDER BY timestamp DESC LIMIT 10")
    fun getLast10Alerts(): Flow<List<AlertRecord>>
}