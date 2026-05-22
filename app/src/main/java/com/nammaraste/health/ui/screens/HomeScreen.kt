package com.nammaraste.health.ui.screens

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammaraste.health.AppData
import com.nammaraste.health.Road
import com.nammaraste.health.ui.viewmodels.ReportViewModel
import com.nammaraste.health.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isKannada: Boolean,
    onToggleLanguage: () -> Unit,
    onNavigateToReport: () -> Unit,
    onNavigateToMap: () -> Unit,
    viewModel: ReportViewModel
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("all") }
    val reports by viewModel.allReports.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // ── Top Bar with Language Toggle ──
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isKannada) "ನಮ್ಮ ರಸ್ತೆ" else "Namma Raste",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (isKannada) "ಆರೋಗ್ಯ ಟ್ರ್ಯಾಕರ್" else "Health Tracker",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // Language Toggle Chip
                FilledTonalButton(
                    onClick = onToggleLanguage,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(
                        Icons.Filled.Translate,
                        contentDescription = "Toggle Language",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isKannada) "English" else "ಕನ್ನಡ",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        // ── Hero Dashboard Card ──
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    NammaGreen,
                                    NammaGreenDark
                                )
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        // Tag
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = if (isKannada) "📍 ಬೆಂಗಳೂರು ಡ್ಯಾಶ್‌ಬೋರ್ಡ್" else "📍 Bengaluru Dashboard",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (isKannada) "ನಿಮ್ಮ ರಸ್ತೆ ತಿಳಿಯಿರಿ.\nನಿಮ್ಮ ನಗರ ರಕ್ಷಿಸಿ."
                            else "Know Your Road.\nProtect Your City.",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            lineHeight = 30.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isKannada)
                                "ಪ್ರತಿ ರಸ್ತೆಗೆ ಡಿಜಿಟಲ್ ಲೈಫ್-ಬುಕ್. ನಾಗರಿಕರೇ ರಸ್ತೆ ಆಡಿಟರ್‌ಗಳು."
                            else "Every road gets a Digital Life-Book. Citizens are Road Auditors.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatItem("${AppData.roads.size}", if (isKannada) "ರಸ್ತೆಗಳು" else "Roads")
                            StatItem("${reports.size}", if (isKannada) "ವರದಿಗಳು" else "Reports")
                            val avgHealth = AppData.roads.map { it.health }.average().toInt()
                            StatItem("$avgHealth%", if (isKannada) "ಸರಾಸರಿ" else "Avg Health")
                        }
                    }
                }
            }
        }

        // ── Impact Grid (2x2) ──
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ImpactCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Build,
                    value = "12",
                    label = if (isKannada) "ಈ ತಿಂಗಳು ದುರಸ್ತಿ" else "Repaired this month",
                    iconTint = NammaGreen,
                    onClick = {
                        Toast.makeText(context, if (isKannada) "12 ರಸ್ತೆಗಳನ್ನು ಯಶಸ್ವಿಯಾಗಿ ದುರಸ್ತಿ ಮಾಡಲಾಗಿದೆ!" else "12 roads successfully repaired this month!", Toast.LENGTH_SHORT).show()
                    }
                )
                ImpactCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Warning,
                    value = "4",
                    label = if (isKannada) "ನಿರ್ಣಾಯಕ ಎಚ್ಚರಿಕೆ" else "Critical alerts",
                    iconTint = HealthCritical,
                    onClick = {
                        selectedFilter = "bad"
                        Toast.makeText(context, if (isKannada) "ನಿರ್ಣಾಯಕ ರಸ್ತೆಗಳನ್ನು ತೋರಿಸಲಾಗುತ್ತಿದೆ" else "Showing critical roads", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ImpactCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.People,
                    value = "1.2K",
                    label = if (isKannada) "ನಾಗರಿಕ ಆಡಿಟರ್" else "Citizen auditors",
                    iconTint = NammaPurple,
                    onClick = {
                        Toast.makeText(context, if (isKannada) "ಸಕ್ರಿಯವಾಗಿ ಕೊಡುಗೆ ನೀಡುತ್ತಿರುವ 1,200 ನಾಗರಿಕರು" else "1,200 citizens actively contributing", Toast.LENGTH_SHORT).show()
                    }
                )
                ImpactCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.EmojiEvents,
                    value = "₹3.1Cr",
                    label = if (isKannada) "ಸಾರ್ವಜನಿಕ ಉಳಿತಾಯ" else "Public savings",
                    iconTint = Saffron,
                    onClick = {
                        Toast.makeText(context, if (isKannada) "ಡಿಜಿಟಲ್ ಆಡಿಟಿಂಗ್ ಮೂಲಕ ₹3.1 ಕೋಟಿ ಉಳಿತಾಯವಾಗಿದೆ" else "₹3.1Cr saved through digital auditing", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        // ── Quick Action Buttons ──
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionButton(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.CameraAlt,
                    label = if (isKannada) "ಹಾನಿ ವರದಿ" else "Report Damage",
                    gradient = listOf(Saffron, SaffronLight),
                    onClick = onNavigateToReport
                )
                QuickActionButton(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Map,
                    label = if (isKannada) "ನಕ್ಷೆ" else "Map View",
                    gradient = listOf(NammaPurple, NammaPurpleLight),
                    onClick = onNavigateToMap
                )
            }
        }

        // ── Section Title: Road Health ──
        item {
            SectionTitle(
                text = if (isKannada) "ರಸ್ತೆ ಆರೋಗ್ಯ ಸೂಚ್ಯಂಕ" else "Road Health Index",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )
        }

        // ── Filter Chips ──
        item {
            LazyRow(
                modifier = Modifier.padding(bottom = 12.dp),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf(
                    "all" to if (isKannada) "ಎಲ್ಲಾ" else "All",
                    "good" to if (isKannada) "ಉತ್ತಮ" else "Healthy",
                    "warn" to if (isKannada) "ಸಾಧಾರಣ" else "Moderate",
                    "bad" to if (isKannada) "ನಿರ್ಣಾಯಕ" else "Critical"
                )
                items(filters) { (key, label) ->
                    FilterChip(
                        selected = selectedFilter == key,
                        onClick = { selectedFilter = key },
                        label = { Text(label, style = MaterialTheme.typography.labelMedium) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NammaGreen,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // ── Road Cards ──
        val filteredRoads = if (selectedFilter == "all") AppData.roads
        else AppData.roads.filter { AppData.healthClass(it.health) == selectedFilter }

        items(filteredRoads) { road ->
            RoadHealthCard(
                road = road,
                isKannada = isKannada,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )
        }
    }
}

// ─── Reusable Composables ───

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.6f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImpactCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun QuickActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "scale_animation"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(),
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(gradient), RoundedCornerShape(16.dp))
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(16.dp)
                .background(NammaGreen, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.titleSmall,
            letterSpacing = 0.8.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun RoadHealthCard(road: Road, isKannada: Boolean, modifier: Modifier = Modifier) {
    val healthColor = when (AppData.healthClass(road.health)) {
        "good" -> HealthGood
        "warn" -> HealthModerate
        else -> HealthCritical
    }
    val healthBgColor = when (AppData.healthClass(road.health)) {
        "good" -> Color(0xFFDCFCE7)
        "warn" -> Color(0xFFFEF3C7)
        else -> Color(0xFFFEE2E2)
    }
    val healthTextColor = when (AppData.healthClass(road.health)) {
        "good" -> Color(0xFF166534)
        "warn" -> Color(0xFF92400E)
        else -> Color(0xFF991B1B)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isKannada) road.nameKn else road.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${if (isKannada) road.zoneKn else road.zone} · ${road.length}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = healthBgColor
                ) {
                    Text(
                        text = if (isKannada) AppData.healthLabelKn(road.health) else AppData.healthLabel(road.health),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = healthTextColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Health bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isKannada) "ರಸ್ತೆ ಆರೋಗ್ಯ ಸೂಚ್ಯಂಕ" else "Road Health Index",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${road.health}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            val animatedProgress by animateFloatAsState(
                targetValue = road.health / 100f,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                label = "progress_animation"
            )
            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(10.dp)),
                color = healthColor,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Meta row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Engineering,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = road.contractor,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = 120.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Report,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${road.reports.size} ${if (isKannada) "ವರದಿ" else "report"}${if (road.reports.size != 1 && !isKannada) "s" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
