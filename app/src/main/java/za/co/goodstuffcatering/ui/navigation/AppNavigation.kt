package za.co.goodstuffcatering.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import za.co.goodstuffcatering.ui.auth.LoginScreen
import za.co.goodstuffcatering.ui.auth.RegistrationScreen
import za.co.goodstuffcatering.ui.booking.BookingScreen
import za.co.goodstuffcatering.ui.booking.QuoteCheckoutScreen
import za.co.goodstuffcatering.ui.dashboard.DashboardScreen
import za.co.goodstuffcatering.ui.landing.LandingScreen
import za.co.goodstuffcatering.ui.menu.MenuDiscoveryScreen
import za.co.goodstuffcatering.ui.receipt.OrderDetailsScreen
import za.co.goodstuffcatering.ui.settings.SettingsScreen

/**
 * A sealed class defining all the routes (screens) in the app.
 * It acts as a central dictionary so we don't make typos in our navigation strings.
 */
sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Login : Screen("login")
    object Registration : Screen("registration")
    object Dashboard : Screen("dashboard")
    object Menu : Screen("menu")
    object BookingForm : Screen("booking_form")
    object Checkout : Screen("checkout")
    object OrderDetails : Screen("order_details/{bookingId}") {
        fun createRoute(bookingId: Int) = "order_details/$bookingId"
    }
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    // NavHost acts as the container for the navigation graph.
    // startDestination defines the very first screen shown when the app opens.
    NavHost(navController = navController, startDestination = Screen.Landing.route) {

        // 1. Landing Screen
        composable(Screen.Landing.route) {
            LandingScreen(navController = navController)
        }

        // 2. Login Screen
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        // 3. Registration Screen (POPIA & Phone Capture)
        composable(Screen.Registration.route) {
            RegistrationScreen(navController = navController)
        }

        // 4. Dashboard (Main Hub)
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }

        // 5. Menu Discovery
        composable(Screen.Menu.route) {
            MenuDiscoveryScreen(navController = navController)
        }

        // 6. Booking Form
        composable(Screen.BookingForm.route) {
            BookingScreen(navController = navController)
        }

        // 7. Checkout (Quote Breakdown)
        composable(Screen.Checkout.route) {
            QuoteCheckoutScreen(navController = navController)
        }

        // 8. Order Details & Receipt
        // Change the composable to accept the argument
        composable(
            route = Screen.OrderDetails.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getInt("bookingId") ?: 0
            OrderDetailsScreen(navController = navController, bookingId = bookingId)
        }

        // 9. Settings (POPIA Delete Account)
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}