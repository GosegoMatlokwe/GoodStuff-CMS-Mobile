package za.co.goodstuffcatering.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import za.co.goodstuffcatering.ui.auth.LoginScreen
import za.co.goodstuffcatering.ui.booking.BookingScreen
import za.co.goodstuffcatering.ui.dashboard.DashboardScreen
import za.co.goodstuffcatering.ui.landing.LandingScreen // Import Landing
import za.co.goodstuffcatering.ui.settings.SettingsScreen

sealed class Screen(val route: String) {
    object Landing : Screen("landing") // Add Landing
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object Booking : Screen("booking")
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    // Start destination is now Landing
    NavHost(navController = navController, startDestination = Screen.Landing.route) {
        composable(Screen.Landing.route) {
            LandingScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        // ... keep the rest exactly the same ...
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(Screen.Booking.route) {
            BookingScreen(navController = navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}