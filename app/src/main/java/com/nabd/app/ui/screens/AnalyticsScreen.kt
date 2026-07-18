package com.nabd.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabd.app.data.model.*
import com.nabd.app.ui.theme.*
import com.nabd.app.ui.viewmodel.AnalyticsViewModel

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel,
    patientId: String,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    AnalyticsContent(uiState = uiState, onBack = onBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsContent(
    uiState: AnalyticsUiState,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("AI Patient Insights", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = NabdSecondary)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NabdSecondary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = NabdBackground)
            )
        },
        containerColor = NabdBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RiskGauge(score = uiState.riskScore, modifier = Modifier.size(140.dp))
                    
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text("Overall Risk Score", style = MaterialTheme.typography.labelMedium, color = NabdTextMuted)
                        Text(
                            text = if (uiState.riskScore < 0.3f) "Low Risk" else "Elevated Risk",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (uiState.riskScore < 0.3f) Color(0xFF4CAF50) else NabdPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Based on 30-day predictive modeling",
                            style = MaterialTheme.typography.bodySmall,
                            color = NabdTextMuted
                        )
                    }
                }
            }

            item {
                ComplianceTrendCard(points = uiState.complianceTrend)
            }

            item {
                Text("Predictive AI Insights", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = NabdSecondary))
            }

            items(uiState.insights) { insight ->
                InsightCard(insight = insight)
            }

            item {
                AdherenceStatsGrid(
                    total = uiState.totalDoses,
                    missed = uiState.missedDoses,
                    compliantDays = uiState.compliantDays
                )
            }
        }
    }
}

@Composable
fun RiskGauge(score: Float, modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 12.dp.toPx()
            val innerRadius = (size.minDimension - strokeWidth) / 2
            
            // Background arc
            drawArc(
                color = Color.LightGray.copy(alpha = 0.2f),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            
            // Score arc
            drawArc(
                brush = Brush.sweepGradient(
                    0f to Color(0xFF4CAF50),
                    0.5f to Color(0xFFFFEB3B),
                    1f to NabdPrimary
                ),
                startAngle = 135f,
                sweepAngle = 270f * score,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(score * 100).toInt()}%",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = NabdSecondary)
            )
            Text("SCORE", style = MaterialTheme.typography.labelSmall, color = NabdTextMuted)
        }
    }
}

@Composable
fun ComplianceTrendCard(points: List<AdherencePoint>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = NabdSecondary)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Adherence Trend", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Simplified Trend Visualization
            Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                if (points.isEmpty()) return@Canvas
                
                val width = size.width
                val height = size.height
                val stepX = width / (points.size - 1)
                
                // Draw path
                val path = androidx.compose.ui.graphics.Path().apply {
                    points.forEachIndexed { index, point ->
                        val x = index * stepX
                        val y = height - (point.percentage * height)
                        if (index == 0) moveTo(x, y) else lineTo(x, y)
                    }
                }
                
                drawPath(
                    path = path,
                    color = NabdSecondary,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )
                
                // Area fill
                val fillPath = androidx.compose.ui.graphics.Path().apply {
                    addPath(path)
                    lineTo(width, height)
                    lineTo(0f, height)
                    close()
                }
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(NabdSecondary.copy(alpha = 0.1f), Color.Transparent)
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Text("Last 30 Days Activity", style = MaterialTheme.typography.labelSmall, color = NabdTextMuted)
        }
    }
}

@Composable
fun InsightCard(insight: AIInsight) {
    val accentColor = when (insight.priority) {
        InsightPriority.CRITICAL -> NabdPrimary
        InsightPriority.ADVISORY -> NabdSecondary
        InsightPriority.OPTIMIZATION -> Color(0xFF673AB7)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.Top) {
            Surface(
                color = accentColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(insight.icon, contentDescription = null, tint = accentColor)
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(insight.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = NabdSecondary))
                Spacer(modifier = Modifier.height(4.dp))
                Text(insight.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Composable
fun AdherenceStatsGrid(total: Int, missed: Int, compliantDays: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        StatItem(label = "Total Doses", value = total.toString(), modifier = Modifier.weight(1f))
        StatItem(label = "Missed", value = missed.toString(), color = NabdPrimary, modifier = Modifier.weight(1f))
        StatItem(label = "Compliant Days", value = compliantDays.toString(), modifier = Modifier.weight(1f))
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color = NabdSecondary, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = color))
            Text(label, style = MaterialTheme.typography.labelSmall, color = NabdTextMuted, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnalyticsPreview() {
    NABDTheme {
        AnalyticsContent(
            uiState = AnalyticsUiState(
                complianceTrend = List(10) { AdherencePoint(it, (70..100).random().toFloat() / 100f) },
                insights = listOf(
                    AIInsight("Urgent: Missed Dose", "High risk drug missed.", InsightPriority.CRITICAL, Icons.Default.Warning)
                )
            ),
            onBack = {}
        )
    }
}
