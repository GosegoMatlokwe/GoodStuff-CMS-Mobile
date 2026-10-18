package za.co.goodstuffcatering.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import za.co.goodstuffcatering.ui.common.UiState
import za.co.goodstuffcatering.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    // Collect the UI state from the ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Scaffold provides the basic layout structure (TopBar, BottomBar, FAB, Content area)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    // Settings Icon button in the top right corner
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            // FAB for creating a new booking
            FloatingActionButton(onClick = {
                // FIXED: Changed Screen.Booking to Screen.BookingForm to match AppNavigation
                navController.navigate(Screen.BookingForm.route)
            }) {
                Icon(Icons.Default.Add, contentDescription = "New Booking")
            }
        }
    ) { padding ->
        // Box fills the screen below the TopBar and pads accordingly
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {

            // The 'when' statement forces us to handle every possible UI state safely
            when (val state = uiState) {
                is UiState.Loading -> {
                    // Show a spinner in the center of the screen
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is UiState.Error -> {
                    // Show the error message and a Retry button
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { state.retryAction?.invoke() }) {
                            Text("Retry")
                        }
                    }
                }

                is UiState.Success -> {
                    // If the list is empty, show a friendly message
                    if (state.data.isEmpty()) {
                        Text(
                            text = "No upcoming bookings. Create one!",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        // If we have data, show it in a scrolling list (LazyColumn)
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Loop through the list of bookings and create a Card for each
                            items(state.data) { booking ->
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("Event: ${booking.eventType}", style = MaterialTheme.typography.titleMedium)
                                        Text("Date: ${booking.eventDate}")
                                        Text("Status: ${booking.status}", color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}