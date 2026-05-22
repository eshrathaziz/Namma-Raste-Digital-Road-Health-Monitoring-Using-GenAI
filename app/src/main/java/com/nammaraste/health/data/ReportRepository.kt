package com.nammaraste.health.data

import kotlinx.coroutines.flow.Flow

class ReportRepository(private val reportDao: ReportDao) {
    val allReports: Flow<List<ReportEntity>> = reportDao.getAllReports()

    suspend fun insert(report: ReportEntity) {
        reportDao.insertReport(report)
    }

    suspend fun update(report: ReportEntity) {
        reportDao.updateReport(report)
    }

    suspend fun delete(report: ReportEntity) {
        reportDao.deleteReport(report)
    }
}
