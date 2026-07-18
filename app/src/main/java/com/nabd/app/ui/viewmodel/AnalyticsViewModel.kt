package com.nabd.app.ui.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Warning
import androidx.lifecycle.ViewModel
import com.nabd.app.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AnalyticsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        loadAnalyticsData()
    }

    private fun loadAnalyticsData() {
        // Mocking trend data for 30 days
        val mockTrend = List(30) { i ->
            AdherencePoint(i + 1, (80..100).random().toFloat() / 100f)
        }

        val mockInsights = listOf(
            AIInsight(
                title = "High-Priority Missed Dose",
                description = "Antiplatelet medication missed 2 days ago. Urgent follow-up recommended.",
                priority = InsightPriority.CRITICAL,
                icon = Icons.Default.Warning
            ),
            AIInsight(
                title = "Optimized Nudge Time",
                description = "Patient is 40% more responsive to reminders between 09:00 - 09:30 AM.",
                priority = InsightPriority.OPTIMIZATION,
                icon = Icons.Default.Lightbulb
            ),
            AIInsight(
                title = "Habit Trend: Improving",
                description = "Adherence has increased by 12% compared to the previous 30-day window.",
                priority = InsightPriority.ADVISORY,
                icon = Icons.Default.AutoGraph
            )
        )

        _uiState.update { 
            it.copy(
                riskScore = 0.22f, // Low-Medium Risk
                complianceTrend = mockTrend,
                insights = mockInsights,
                totalDoses = 240,
                missedDoses = 12,
                compliantDays = 26
            )
        }
    }
}
