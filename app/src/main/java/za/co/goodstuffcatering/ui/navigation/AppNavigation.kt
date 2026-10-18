package za.co.goodstuffcatering.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument // <--- THIS WAS MISSING
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

    // UPDATED: Accepts pricePerHead so we can use the actual menu price
    object BookingForm : Screen("booking_form?price={price}") {
        fun createRoute(price: Double) = "booking_form?price=$price"
    }

    object Checkout : Screen("checkout")
    object OrderDetails : Screen("order_details/{bookingId}") {
        fun createRoute(bookingId: Int) = "order_details/$bookingId"
    }
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Landing.route) {

        composable(Screen.Landing.route) { LandingScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Registration.route) { RegistrationScreen(navController) }
        composable(Screen.Dashboard.route) { DashboardScreen(navController) }
        composable(Screen.Menu.route) { MenuDiscoveryScreen(navController) }

        // UPDATED: Read the price argument and pass it to BookingScreen
        composable(
            route = Screen.BookingForm.route,
            arguments = listOf(navArgument("price") {
                type = NavType.StringType
                defaultValue = "150.0" // Default if no menu was selected
            })
        ) { backStackEntry ->
            val price = backStackEntry.arguments?.getString("price")?.toDoubleOrNull() ?: 150.0
            BookingScreen(navController = navController, initialPricePerHead = price)
        }

        composable(Screen.Checkout.route) { QuoteCheckoutScreen(navController) }

        composable(
            route = Screen.OrderDetails.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getInt("bookingId") ?: 0
            OrderDetailsScreen(navController = navController, bookingId = bookingId)
        }

        composable(Screen.Settings.route) { SettingsScreen(navController) }
    }
}