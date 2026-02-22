package com.example.blindguard

import android.content.Context
import kotlinx.coroutines.flow.Flow

class AlertRepository(context: Context) {

    private val dao = AppDatabase.getDatabase(context).alertDao()

    fun getAlerts(): Flow<List<AlertRecord>> = dao.getLast10Alerts()

    suspend fun addAlert(alert: AlertRecord) {
        dao.insert(alert)
    }
}