package com.nammaraste.health

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammaraste.health.ui.screens.*
import com.nammaraste.health.ui.viewmodels.*
import com.nammaraste.health.ui.theme.NammaGreen
import com.nammaraste.health.ui.theme.NammaRasteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // AppData.init(this) // Handled in Application class
        enableEdgeToEdge()
        setContent {
            val app = application as NammaRasteApplication
            val viewModel: ReportViewModel = viewModel(
                factory = ReportViewModelFactory(app.repository)
            )
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(app.authRepository)
            )
            NammaRasteTheme(darkTheme = false) {
                NammaRasteApp(viewModel, authViewModel)
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object Report : Screen("report")
    object Map : Screen("map")
    object AdminDashboard : Screen("admin_dashboard")
}

data class BottomNavItem(
    val screen: Screen,
    val labelEn: String,
    val labelKn: String,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NammaRasteApp(viewModel: ReportViewModel, authViewModel: AuthViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Intro) }
    var isKannada by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    val navItems = listOf(
        BottomNavItem(Screen.Home, "Home", "ಮುಖಪುಟ", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem(Screen.Report, "Report", "ವರದಿ", Icons.Filled.CameraAlt, Icons.Outlined.CameraAlt),
        BottomNavItem(Screen.Map, "Map", "ನಕ್ಷೆ", Icons.Filled.Map, Icons.Outlined.Map)
    )

    val showBottomBar = currentScreen !in listOf(Screen.Intro, Screen.Login, Screen.SignUp, Screen.AdminDashboard)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    tonalElevation = 0.dp
                ) {
                    navItems.forEach { item ->
                        val selected = currentScreen == item.screen
                        NavigationBarItem(
                            selected = selected,
                            onClick = { currentScreen = item.screen },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.iconSelected else item.iconUnselected,
                                    contentDescription = item.labelEn
                                )
                            },
                            label = {
                                Text(
                                    text = if (isKannada) item.labelKn else item.labelEn,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NammaGreen,
                                selectedTextColor = NammaGreen,
                                indicatorColor = NammaGreen.copy(alpha = 0.12f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    (slideInHorizontally(
                        animationSpec = tween(300),
                        initialOffsetX = { fullWidth -> fullWidth }
                    ) + fadeIn(animationSpec = tween(300))).togetherWith(
                        slideOutHorizontally(
                            animationSpec = tween(300),
                            targetOffsetX = { fullWidth -> -fullWidth }
                        ) + fadeOut(animationSpec = tween(300))
                    )
                },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    Screen.Intro -> IntroScreen(
                        onGetStarted = { currentScreen = Screen.SignUp }
                    )
                    Screen.Login -> LoginScreen(
                        viewModel = authViewModel,
                        onNavigateToSignUp = { currentScreen = Screen.SignUp },
                        onLoginSuccess = { currentScreen = Screen.Home }
                    )
                    Screen.SignUp -> SignUpScreen(
                        viewModel = authViewModel,
                        onNavigateToLogin = { currentScreen = Screen.Login },
                        onSignUpSuccess = { currentScreen = Screen.Home }
                    )
                    Screen.Home -> HomeScreen(
                        isKannada = isKannada,
                        onToggleLanguage = { isKannada = !isKannada },
                        onNavigateToReport = { currentScreen = Screen.Report },
                        onNavigateToMap = { currentScreen = Screen.Map },
                        onNavigateToAdmin = { currentScreen = Screen.AdminDashboard },
                        isAdmin = currentUser?.isAdmin == true,
                        viewModel = viewModel
                    )
                    Screen.Report -> ReportScreen(
                        isKannada = isKannada,
                        onReportSubmitted = { currentScreen = Screen.Home },
                        viewModel = viewModel
                    )
                    Screen.Map -> MapScreen(
                        isKannada = isKannada,
                        viewModel = viewModel
                    )
                    Screen.AdminDashboard -> AdminDashboardScreen(
                        viewModel = viewModel,
                        onBack = { currentScreen = Screen.Home }
                    )
                }
            }
        }
    }
}
