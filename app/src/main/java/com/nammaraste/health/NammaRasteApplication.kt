package com.nammaraste.health

import android.app.Application
import com.nammaraste.health.data.AppDatabase
import com.nammaraste.health.data.AuthRepository
import com.nammaraste.health.data.ReportRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NammaRasteApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ReportRepository(database.reportDao()) }
    val authRepository by lazy { AuthRepository(database.userDao()) }

    override fun onCreate() {
        super.onCreate()
        AppData.init(this)
    }
}
