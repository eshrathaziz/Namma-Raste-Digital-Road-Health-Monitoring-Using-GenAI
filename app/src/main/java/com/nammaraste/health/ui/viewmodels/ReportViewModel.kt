package com.nammaraste.health.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nammaraste.health.data.ReportEntity
import com.nammaraste.health.data.ReportRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReportViewModel(private val repository: ReportRepository) : ViewModel() {

    val allReports: StateFlow<List<ReportEntity>> = repository.allReports
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insert(report: ReportEntity) = viewModelScope.launch {
        repository.insert(report)
    }

    fun update(report: ReportEntity) = viewModelScope.launch {
        repository.update(report)
    }

    fun delete(report: ReportEntity) = viewModelScope.launch {
        repository.delete(report)
    }

    fun cycleSeverity(report: ReportEntity) = viewModelScope.launch {
        val severities = listOf(
            "Minor — surface wear",
            "Moderate — needs repair",
            "Major — dangerous",
            "Critical — road unusable"
        )
        val currentIndex = severities.indexOfFirst { it.startsWith(report.severity.split(" —").first()) }
        val nextIndex = (currentIndex + 1) % severities.size
        val nextSeverity = severities[nextIndex]
        repository.update(report.copy(severity = nextSeverity))
    }
}

class ReportViewModelFactory(private val repository: ReportRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReportViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
