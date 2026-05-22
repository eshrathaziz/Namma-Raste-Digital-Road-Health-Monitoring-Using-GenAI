package com.nammaraste.health.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.nammaraste.health.AppData
import com.nammaraste.health.data.ReportEntity
import com.nammaraste.health.ui.viewmodels.ReportViewModel
import com.nammaraste.health.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    isKannada: Boolean,
    onReportSubmitted: () -> Unit,
    viewModel: ReportViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var selectedRoad by remember { mutableStateOf("") }
    var roadDropdownExpanded by remember { mutableStateOf(false) }
    var selectedDamageType by remember { mutableStateOf("") }
    var selectedSeverity by remember { mutableStateOf("") }
    var severityExpanded by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }
    var reporterName by remember { mutableStateOf("") }
    var showAiImproved by remember { mutableStateOf(false) }
    var aiLoading by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    var currentGps by remember { mutableStateOf("13.0336°N, 77.6253°E") }
    var currentAddress by remember { mutableStateOf("560045 HKBK College of Engineering, Nagawara, Bangalore") }
    var isLocationLoading by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                capturedImageUri = tempImageUri
            }
        }
    )

    fun createImageUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("PNG_${timeStamp}_", ".png", storageDir)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    @SuppressLint("MissingPermission")
    fun fetchLocation() {
        isLocationLoading = true
        val cancellationTokenSource = CancellationTokenSource()
        
        // Use a continuous location update request for better accuracy initially
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val lng = location.longitude
                currentGps = String.format("%.4f°N, %.4f°E", lat, lng)
                
                scope.launch {
                    try {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(lat, lng, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            val addressText = address.getAddressLine(0) ?: "Address not found"
                            currentAddress = addressText
                        } else {
                            currentAddress = "Address not found"
                        }
                    } catch (e: Exception) {
                        currentAddress = "Geocoding failed"
                    } finally {
                        isLocationLoading = false
                    }
                }
            } else {
                currentGps = "GPS Not Found"
                currentAddress = "Check GPS/Internet settings"
                isLocationLoading = false
            }
        }.addOnFailureListener {
            currentGps = "Location Error"
            currentAddress = "Failed to fetch location"
            isLocationLoading = false
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val uri = createImageUri()
                tempImageUri = uri
                cameraLauncher.launch(uri)
            }
        }
    )

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                fetchLocation()
            } else {
                currentGps = "Permission Denied"
                currentAddress = "Location access needed"
            }
        }
    )

    fun launchCamera() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val uri = createImageUri()
            tempImageUri = uri
            cameraLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun requestLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fetchLocation()
        } else {
            locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    // Initial location request
    LaunchedEffect(Unit) {
        requestLocation()
    }

    // GPS pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "gps_pulse")
    val gpsPulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gps_alpha"
    )

    val damageTypes = listOf(
        "🕳️" to if (isKannada) "ಗುಂಡಿ" else "Pothole",
        "〰️" to if (isKannada) "ಬಿರುಕು" else "Crack",
        "🌊" to if (isKannada) "ನೀರು ನಿಲ್ಲುವಿಕೆ" else "Water Log",
        "↘️" to if (isKannada) "ಕುಸಿತ" else "Subsidence",
        "💢" to if (isKannada) "ಅಂಚು ಒಡೆಯುವಿಕೆ" else "Edge Break",
        "📌" to if (isKannada) "ಇತರೆ" else "Other"
    )

    val severities = listOf(
        if (isKannada) "ಸಣ್ಣ — ಮೇಲ್ಮೈ ಸವೆತ" else "Minor — surface wear",
        if (isKannada) "ಸಾಧಾರಣ — ದುರಸ್ತಿ ಬೇಕು" else "Moderate — needs repair",
        if (isKannada) "ಗಂಭೀರ — ಅಪಾಯಕಾರಿ" else "Major — dangerous",
        if (isKannada) "ನಿರ್ಣಾಯಕ — ರಸ್ತೆ ಬಳಸಲಾಗದು" else "Critical — road unusable"
    )

    if (showSuccess) {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = NammaGreen,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isKannada) "ವರದಿ ಯಶಸ್ವಿಯಾಗಿ ಸಲ್ಲಿಸಲಾಗಿದೆ!" else "Report Submitted Successfully!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = NammaGreen,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isKannada) "ಧನ್ಯವಾದ, ನಾಗರಿಕ ಆಡಿಟರ್!" else "Thank you, Citizen Auditor!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        showSuccess = false
                        onReportSubmitted()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NammaGreen)
                ) {
                    Text(if (isKannada) "ಮುಂದುವರಿಯಿರಿ" else "Continue")
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(
                text = if (isKannada) "ಹಾನಿ ವರದಿ ಸಲ್ಲಿಸಿ" else "File a Damage Report",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    FormLabel(if (isKannada) "ರಸ್ತೆ ಹೆಸರು" else "Road Name")
                    ExposedDropdownMenuBox(
                        expanded = roadDropdownExpanded,
                        onExpandedChange = { roadDropdownExpanded = !roadDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedRoad,
                            onValueChange = { selectedRoad = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            placeholder = {
                                Text(
                                    if (isKannada) "ರಸ್ತೆ ಹುಡುಕಿ ಅಥವಾ ಆಯ್ಕೆಮಾಡಿ..." else "Search or select road...",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            leadingIcon = {
                                Icon(Icons.Outlined.Search, contentDescription = null, modifier = Modifier.size(20.dp))
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roadDropdownExpanded) },
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NammaGreen,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = roadDropdownExpanded,
                            onDismissRequest = { roadDropdownExpanded = false }
                        ) {
                            val filteredRoads = AppData.roads.filter { 
                                it.name.contains(selectedRoad, ignoreCase = true) || 
                                it.nameKn.contains(selectedRoad, ignoreCase = true) 
                            }
                            
                            if (filteredRoads.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No roads found") },
                                    onClick = { }
                                )
                            } else {
                                filteredRoads.forEach { road ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                if (isKannada) road.nameKn else road.name,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        },
                                        onClick = {
                                            selectedRoad = if (isKannada) road.nameKn else road.name
                                            roadDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))

                    FormLabel(if (isKannada) "ಹಾನಿ ಪ್ರಕಾರ" else "Type of Damage")
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            damageTypes.take(3).forEach { (icon, label) ->
                                DamageTypeButton(
                                    modifier = Modifier.weight(1f),
                                    icon = icon,
                                    label = label,
                                    selected = selectedDamageType == label,
                                    onClick = { selectedDamageType = label }
                                )
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            damageTypes.drop(3).forEach { (icon, label) ->
                                DamageTypeButton(
                                    modifier = Modifier.weight(1f),
                                    icon = icon,
                                    label = label,
                                    selected = selectedDamageType == label,
                                    onClick = { selectedDamageType = label }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))

                    FormLabel(if (isKannada) "ತೀವ್ರತೆ" else "Severity")
                    ExposedDropdownMenuBox(
                        expanded = severityExpanded,
                        onExpandedChange = { severityExpanded = !severityExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedSeverity,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            placeholder = {
                                Text(
                                    if (isKannada) "ಆಯ್ಕೆಮಾಡಿ..." else "Select...",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = severityExpanded) },
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NammaGreen,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = severityExpanded,
                            onDismissRequest = { severityExpanded = false }
                        ) {
                            severities.forEach { sev ->
                                DropdownMenuItem(
                                    text = { Text(sev, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        selectedSeverity = sev
                                        severityExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))

                    FormLabel(if (isKannada) "ಫೋಟೋ ಸಾಕ್ಷ್ಯ" else "Photo Evidence")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .border(
                                width = 2.dp,
                                color = if (capturedImageUri != null) NammaGreen else MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .clickable { launchCamera() },
                        contentAlignment = Alignment.Center
                    ) {
                        if (capturedImageUri != null) {
                            AsyncImage(
                                model = capturedImageUri,
                                contentDescription = "Captured road damage",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Surface(
                                color = Color.Black.copy(alpha = 0.5f),
                                shape = CircleShape,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(8.dp)
                                    .size(32.dp)
                            ) {
                                Icon(Icons.Filled.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.padding(6.dp))
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Outlined.CameraAlt, contentDescription = null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = if (isKannada) "ಫೋಟೋ ತೆಗೆಯಲು ಟ್ಯಾಪ್ ಮಾಡಿ" else "Tap to capture photo", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = if (isKannada) "GPS ಸ್ವಯಂ-ಸೆರೆಹಿಡಿಯಲಾಗುತ್ತದೆ" else "GPS coordinates auto-captured", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFEFF6FF),
                        border = ButtonDefaults.outlinedButtonBorder,
                        modifier = Modifier.fillMaxWidth().clickable { requestLocation() }
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            if (isLocationLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = Color(0xFF3B82F6))
                            } else {
                                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF3B82F6).copy(alpha = gpsPulse)))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = "GPS: $currentGps", style = MaterialTheme.typography.labelMedium, color = Color(0xFF1D4ED8), fontWeight = FontWeight.Bold)
                                Text(
                                    text = currentAddress,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF1D4ED8).copy(alpha = 0.7f),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh Location", tint = Color(0xFF1D4ED8), modifier = Modifier.size(18.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))

                    FormLabel(if (isKannada) "ವಿವರಣೆ (ಐಚ್ಛಿಕ)" else "Description (optional)")
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        placeholder = { Text(if (isKannada) "ಸಮಸ್ಯೆಯನ್ನು ವಿವರಿಸಿ..." else "Describe the issue...", style = MaterialTheme.typography.bodyMedium) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NammaGreen, unfocusedBorderColor = MaterialTheme.colorScheme.outline)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    AnimatedVisibility(visible = description.isNotEmpty()) {
                        OutlinedButton(
                            onClick = { aiLoading = true; showAiImproved = false },
                            shape = RoundedCornerShape(10.dp),
                            border = ButtonDefaults.outlinedButtonBorder,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = NammaPurple),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (aiLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = NammaPurple)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (isKannada) "AI ಸುಧಾರಿಸುತ್ತಿದೆ..." else "AI improving...", style = MaterialTheme.typography.labelMedium)
                                LaunchedEffect(Unit) {
                                    delay(1500)
                                    description = if (isKannada) "ರಸ್ತೆಯ ಮಧ್ಯಭಾಗದಲ್ಲಿ ಅಂದಾಜು 2x3 ಅಡಿ ಗಾತ್ರದ ಗುಂಡಿ ಕಂಡುಬಂದಿದೆ. ಮಳೆ ನೀರು ನಿಂತಿದ್ದು ವಾಹನಗಳ ಸಂಚಾರಕ್ಕೆ ಅಪಾಯಕಾರಿ. ತುರ್ತು ದುರಸ್ತಿ ಅಗತ್ಯ." else "A pothole approximately 2x3 feet in size has been observed in the center of the road. Rainwater accumulation poses a significant hazard to vehicular traffic. Urgent repair is recommended."
                                    aiLoading = false; showAiImproved = true
                                }
                            } else {
                                Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isKannada) "✨ AI ಬಳಸಿ ವಿವರಣೆ ಸುಧಾರಿಸಿ" else "✨ Improve description using AI", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }

                    AnimatedVisibility(visible = showAiImproved) {
                        Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFF3E5F5), modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = NammaPurple, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = if (isKannada) "AI ನಿಂದ ಸುಧಾರಿಸಲಾಗಿದೆ" else "Enhanced by AI", style = MaterialTheme.typography.labelSmall, color = NammaPurple, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))

                    FormLabel(if (isKannada) "ವರದಿಗಾರ ಹೆಸರು" else "Reporter Name")
                    OutlinedTextField(
                        value = reporterName,
                        onValueChange = { reporterName = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(if (isKannada) "ನಿಮ್ಮ ಹೆಸರು (ಐಚ್ಛಿಕ)" else "Your name (optional)", style = MaterialTheme.typography.bodyMedium) },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NammaGreen, unfocusedBorderColor = MaterialTheme.colorScheme.outline)
                    )
                    Spacer(modifier = Modifier.height(22.dp))

                    val isFormValid = true // Debug: Allowing submission for troubleshooting

                    Button(
                        onClick = {
                            if (selectedRoad.isEmpty()) {
                                Toast.makeText(context, "Please select or type a Road Name", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (selectedDamageType.isEmpty()) {
                                Toast.makeText(context, "Please select a Damage Type", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (selectedSeverity.isEmpty()) {
                                Toast.makeText(context, "Please select Severity", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            scope.launch {
                                try {
                                    val report = ReportEntity(
                                        road = selectedRoad,
                                        type = selectedDamageType,
                                        severity = selectedSeverity,
                                        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                                        name = reporterName.ifEmpty { "Anon" },
                                        gps = if (currentGps.contains("Detecting")) "Location Pending" else "$currentGps | $currentAddress",
                                        imageUri = capturedImageUri?.toString()
                                    )
                                    viewModel.insert(report)
                                    showSuccess = true
                                    
                                    // CLEAR ALL FIELDS IMMEDIATELY
                                    selectedRoad = ""
                                    selectedDamageType = ""
                                    selectedSeverity = ""
                                    description = ""
                                    reporterName = ""
                                    capturedImageUri = null
                                    
                                    Toast.makeText(context, "Report Submitted Successfully!", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NammaGreen),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(Icons.Filled.Upload, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (isKannada) "📤 ಹಾನಿ ವರದಿ ಸಲ್ಲಿಸಿ" else "📤 Submit Damage Report", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Composable
private fun FormLabel(text: String) {
    Text(text = text.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 6.dp), letterSpacing = 0.6.sp)
}

@Composable
private fun DamageTypeButton(modifier: Modifier = Modifier, icon: String, label: String, selected: Boolean, onClick: () -> Unit) {
    val borderColor by animateColorAsState(targetValue = if (selected) NammaGreen else MaterialTheme.colorScheme.outline, label = "dmg_border")
    val bgColor by animateColorAsState(targetValue = if (selected) NammaGreen.copy(alpha = 0.08f) else MaterialTheme.colorScheme.background, label = "dmg_bg")
    Surface(modifier = modifier.clip(RoundedCornerShape(10.dp)).clickable(onClick = onClick), shape = RoundedCornerShape(10.dp), color = bgColor, border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(borderColor, borderColor)))) {
        Column(modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = if (selected) NammaGreen else MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold, textAlign = TextAlign.Center, maxLines = 1)
        }
    }
}
