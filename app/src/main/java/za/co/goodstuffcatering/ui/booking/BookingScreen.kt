package za.co.goodstuffcatering.ui.booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import za.co.goodstuffcatering.ui.navigation.Screen // Imported to fix navigation

@Composable
fun BookingScreen(
    navController: NavController,
    viewModel: BookingViewModel = hiltViewModel()
) {
    // Observe the state from the ViewModel
    val guestCount by viewModel.guestCount.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val submitSuccess by viewModel.submitSuccess.collectAsState()

    // Local UI state for the form fields
    var eventType by remember { mutableStateOf("Wedding") }
    var eventDate by remember { mutableStateOf("2026/06/05") }

    // Listen for successful submission to navigate back to the Dashboard
    LaunchedEffect(submitSuccess) {
        if (submitSuccess) {
            navController.navigate(Screen.Dashboard.route) {
                // FIXED: Used Screen.BookingForm.route instead of hardcoded "booking"
                popUpTo(Screen.BookingForm.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("New Catering Booking", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        // Event Type Input
        OutlinedTextField(
            value = eventType,
            onValueChange = { eventType = it },
            label = { Text("Event Type") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Event Date Input (In production, use a DatePicker Dialog here)
        OutlinedTextField(
            value = eventDate,
            onValueChange = { eventDate = it },
            label = { Text("Event Date") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Guest Count Input with Validation
        OutlinedTextField(
            value = guestCount,
            onValueChange = { viewModel.onGuestCountChanged(it) },
            label = { Text("Guest Count") },
            isError = errorMessage != null, // Turns the text field red if there's an error
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Display Error Message
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = { viewModel.submitBooking(eventType, eventDate) },
            // Disable button if there's an error, no guests entered, or currently submitting
            enabled = errorMessage == null && guestCount.isNotEmpty() && !isSubmitting,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isSubmitting) {
                // Show a loading spinner while the network request is happening
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Submit Booking")
            }
        }
    }
}