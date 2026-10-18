package za.co.goodstuffcatering.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import za.co.goodstuffcatering.ui.auth.LoginScreen
import za.co.goodstuffcatering.ui.auth.RegistrationScreen
import za.co.goodstuffcatering.ui.booking.BookingScreen
import za.co.goodstuffcatering.ui.booking.QuoteCheckoutScreen
import za.co.goodstuffcatering.ui.dashboard.DashboardScreen
import za.co.goodstuffcatering.ui.landing.LandingScreen
import za.co.goodstuffcatering.ui.menu.MenuDiscoveryScreen
import za.co.goodstuffcatering.ui.receipt.OrderDetailsScreen
import za.co.goodstuffcatering.ui.settings.SettingsScreen

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Login : Screen("login")
    object Registration : Screen("registration")
    object Dashboard : Screen("dashboard")
    object Menu : Screen("menu")
    object BookingForm : Screen("booking_form")
    object Checkout : Screen("checkout")

    // FIXED: Added the {bookingId} placeholder to the route
    object OrderDetails : Screen("order_details/{bookingId}") {
        // FIXED: Added this helper function to format the route with the real ID
        fun createRoute(bookingId: Int) = "order_details/$bookingId"
    }

    object Settings : Screen("settings")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Landing.route) {

        composable(Screen.Landing.route) {
            LandingScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Registration.route) {
            RegistrationScreen(navController = navController)
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(Screen.Menu.route) {
            MenuDiscoveryScreen(navController = navController)
        }
        composable(Screen.BookingForm.route) {
            BookingScreen(navController = navController)
        }
        composable(Screen.Checkout.route) {
            QuoteCheckoutScreen(navController = navController)
        }

        // FIXED: Updated the composable to accept the argument safely
        composable(
            route = Screen.OrderDetails.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getInt("bookingId") ?: 0
            OrderDetailsScreen(navController = navController, bookingId = bookingId)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}