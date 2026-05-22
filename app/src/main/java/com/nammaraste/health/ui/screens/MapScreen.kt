package com.nammaraste.health.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.nammaraste.health.AppData
import com.nammaraste.health.data.ReportEntity
import com.nammaraste.health.ui.viewmodels.ReportViewModel
import com.nammaraste.health.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(isKannada: Boolean, viewModel: ReportViewModel) {
    var selectedTab by remember { mutableStateOf("success") }
    val dbReports by viewModel.allReports.collectAsState()
    
    var editingReport by remember { mutableStateOf<ReportEntity?>(null) }

    if (editingReport != null) {
        EditReportDialog(
            report = editingReport!!,
            isKannada = isKannada,
            onDismiss = { editingReport = null },
            onSave = { updatedReport ->
                viewModel.update(updatedReport)
                editingReport = null
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // ── Google Map ──
        item {
            val bangalore = LatLng(12.9716, 77.5946)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(bangalore, 11f)
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp, bottom = 12.dp)
                    .height(200.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NightBg)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    AppData.roads.forEach { road ->
                        val roadLatLng = LatLng(road.lat, road.lng)
                        val color = when {
                            road.health >= 80 -> HealthGood
                            road.health >= 55 -> HealthModerate
                            else -> HealthCritical
                        }
                        Marker(
                            state = MarkerState(position = roadLatLng),
                            title = if (isKannada) road.nameKn else road.name,
                            snippet = "${if (isKannada) "ಆರೋಗ್ಯ" else "Health"}: ${road.health}"
                        )
                        Polyline(
                            points = listOf(bangalore, roadLatLng),
                            color = color,
                            width = 5f
                        )
                    }
                }
            }
        }

        // ── Tab Switcher ──
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "success" to ("🏆 " + if (isKannada) "ಅತ್ಯುತ್ತಮ ರಸ್ತೆಗಳು" else "Best Roads"),
                    "contractor" to ("👷 " + if (isKannada) "ಗುತ್ತಿಗೆದಾರರು" else "Contractors"),
                    "reports" to ("📋 " + if (isKannada) "ಎಲ್ಲಾ ವರದಿಗಳು" else "Reports")
                ).forEach { (key, label) ->
                    FilterChip(
                        selected = selectedTab == key,
                        onClick = { selectedTab = key },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NammaGreen,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // ── Tab Content ──
        when (selectedTab) {
            "success" -> {
                item {
                    SectionTitle(
                        text = if (isKannada) "ಅತ್ಯುತ್ತಮ ನಿರ್ವಹಣೆ ರಸ್ತೆಗಳು" else "Best Maintained Roads",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                }
                val sorted = AppData.roads.sortedByDescending { it.health }
                val medals = listOf("🥇", "🥈", "🥉")
                itemsIndexed(sorted) { index, road ->
                    PodiumCard(
                        rank = index + 1,
                        medal = medals.getOrNull(index),
                        roadName = if (isKannada) road.nameKn else road.name,
                        zone = "${if (isKannada) road.zoneKn else road.zone} · ${road.length}",
                        health = road.health,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                }
            }

            "contractor" -> {
                item {
                    SectionTitle(
                        text = if (isKannada) "ಗುತ್ತಿಗೆದಾರ ಡೈರೆಕ್ಟರಿ" else "Contractor Directory",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                }
                items(AppData.contractors) { con ->
                    ContractorCard(
                        contractor = con,
                        isKannada = isKannada,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                }
            }

            "reports" -> {
                item {
                    SectionTitle(
                        text = if (isKannada) "ಹಾನಿ ವರದಿ ಲಾಗ್" else "Damage Report Log",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                }
                items(dbReports) { report ->
                    ReportLogItem(
                        report = report,
                        isKannada = isKannada,
                        onDelete = { viewModel.delete(report) },
                        onEdit = { editingReport = report },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        }
    }
}

// ─── Sub-composables ───

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditReportDialog(
    report: ReportEntity,
    isKannada: Boolean,
    onDismiss: () -> Unit,
    onSave: (ReportEntity) -> Unit
) {
    var road by remember { mutableStateOf(report.road) }
    var type by remember { mutableStateOf(report.type) }
    var severity by remember { mutableStateOf(report.severity) }
    var name by remember { mutableStateOf(report.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isKannada) "ವರದಿ ಸಂಪಾದಿಸಿ" else "Edit Report") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = road,
                    onValueChange = { road = it },
                    label = { Text(if (isKannada) "ರಸ್ತೆ" else "Road") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text(if (isKannada) "ಹಾನಿ ಪ್ರಕಾರ" else "Damage Type") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(if (isKannada) "ಹೆಸರು" else "Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text(text = if (isKannada) "ತೀವ್ರತೆ" else "Severity", style = MaterialTheme.typography.labelMedium)
                val severities = listOf(
                    if (isKannada) "ಸಣ್ಣ — ಮೇಲ್ಮೈ ಸವೆತ" else "Minor — surface wear",
                    if (isKannada) "ಸಾಧಾರಣ — ದುರಸ್ತಿ ಬೇಕು" else "Moderate — needs repair",
                    if (isKannada) "ಗಂಭೀರ — ಅಪಾಯಕಾರಿ" else "Major — dangerous",
                    if (isKannada) "ನಿರ್ಣಾಯಕ — ರಸ್ತೆ ಬಳಸಲಾಗದು" else "Critical — road unusable"
                )
                
                ScrollableRow(modifier = Modifier.fillMaxWidth()) {
                    severities.forEach { sev ->
                        FilterChip(
                            selected = severity == sev,
                            onClick = { severity = sev },
                            label = { Text(sev.split(" —").first(), style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSave(report.copy(road = road, type = type, severity = severity, name = name)) }) {
                Text(if (isKannada) "ಉಳಿಸಿ" else "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isKannada) "ರದ್ದುಗೊಳಿಸಿ" else "Cancel")
            }
        }
    )
}

@Composable
fun ScrollableRow(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    androidx.compose.foundation.lazy.LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item { content() }
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF94A3B8),
            fontSize = 8.sp
        )
    }
}

@Composable
private fun PodiumCard(
    rank: Int,
    medal: String?,
    roadName: String,
    zone: String,
    health: Int,
    modifier: Modifier = Modifier
) {
    val rankBg = when (rank) {
        1 -> Color(0xFFFEF3C7)
        2 -> Color(0xFFF1F5F9)
        3 -> Color(0xFFFEF2F2)
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
    }
    val rankText = when (rank) {
        1 -> Color(0xFF92400E)
        2 -> Color(0xFF475569)
        3 -> Color(0xFF7C2D12)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank badge
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = rankBg,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = medal ?: "#$rank",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = rankText
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = roadName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = zone,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Score
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$health",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = HealthGood
                )
                Text(
                    text = "/100",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ContractorCard(
    contractor: com.nammaraste.health.Contractor,
    isKannada: Boolean,
    modifier: Modifier = Modifier
) {
    val warrantyColor = when {
        contractor.warranty > 60 -> HealthGood
        contractor.warranty > 30 -> HealthModerate
        else -> HealthCritical
    }
    val initials = contractor.name.split(" ").take(2).map { it.first() }.joinToString("")
    val filledStars = kotlin.math.round(contractor.rating).toInt()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(44.dp),
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(listOf(NammaPurple, NammaPurpleLight)),
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = contractor.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = contractor.code,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Roads assigned
            Text(
                text = "${if (isKannada) "ರಸ್ತೆಗಳು" else "Roads"}: ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = contractor.roads.joinToString(", "),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Warranty bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isKannada) "ಖಾತರಿ ಅವಧಿ ಉಳಿದಿದೆ" else "Warranty period remaining",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${contractor.warranty}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = warrantyColor,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = contractor.warranty / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(10.dp)),
                color = warrantyColor,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "★".repeat(filledStars) + "☆".repeat(5 - filledStars),
                    color = Saffron,
                    fontSize = 14.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${contractor.rating}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(${contractor.reviews} ${if (isKannada) "ವಿಮರ್ಶೆಗಳು" else "reviews"})",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Contact info
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "📞 ${contractor.phone}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "✉️ ${contractor.email}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun ReportLogItem(
    report: ReportEntity,
    isKannada: Boolean,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dotColor = when {
        report.severity.contains("Critical") -> HealthCritical
        report.severity.contains("Major") -> HealthCritical
        report.severity.contains("Moderate") -> HealthModerate
        else -> HealthGood
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Timeline dot
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = report.type,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = " ${if (isKannada) "ಮೇಲೆ" else "on"} ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = report.road,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onEdit, modifier = Modifier.size(24.dp)) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = NammaPurple,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = HealthCritical,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))

            // Severity chip
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = report.severity.split("—").first().trim(),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (report.imageUri != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Filled.Image,
                        contentDescription = "Has image",
                        modifier = Modifier.size(14.dp),
                        tint = NammaGreen
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))

            if (report.imageUri != null) {
                Card(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    AsyncImage(
                        model = report.imageUri,
                        contentDescription = "Report image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Text(
                text = "📍 ${report.gps} · ${report.date} · ${if (isKannada) "ಮೂಲಕ" else "By"} ${report.name}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Divider
            Divider(
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
            )
        }
    }
}
