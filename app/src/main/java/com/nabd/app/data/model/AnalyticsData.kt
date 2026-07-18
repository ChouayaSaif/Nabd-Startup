package com.nabd.app.data.model

import androidx.compose.ui.graphics.Color

data class AdherencePoint(
    val day: Int,
    val percentage: Float
)

data class AIInsight(
    val title: String,
    val description: String,
    val priority: InsightPriority,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

enum class InsightPriority {
    CRITICAL, ADVISORY, OPTIMIZATION
}

data class AnalyticsUiState(
    val riskScore: Float = 0.15f, // 0.0 to 1.0
    val complianceTrend: List<AdherencePoint> = emptyList(),
    val insights: List<AIInsight> = emptyList(),
    val totalDoses: Int = 120,
    val missedDoses: Int = 4,
    val compliantDays: Int = 28,
    val isLoading: Boolean = false
)
