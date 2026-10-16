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

@Composable
fun BookingScreen(
    navController: NavController,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val guestCount by viewModel.guestCount.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val submitSuccess by viewModel.submitSuccess.collectAsState()

    var eventType by remember { mutableStateOf("Wedding") }
    var eventDate by remember { mutableStateOf("2026/06/05") }

    LaunchedEffect(submitSuccess) {
        if (submitSuccess) {
            navController.navigate("dashboard") {
                popUpTo("booking") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("New Catering Booking", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = eventType,
            onValueChange = { eventType = it },
            label = { Text("Event Type") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = eventDate,
            onValueChange = { eventDate = it },
            label = { Text("Event Date") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = guestCount,
            onValueChange = { viewModel.onGuestCountChanged(it) },
            label = { Text("Guest Count") },
            isError = errorMessage != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.submitBooking(eventType, eventDate) },
            enabled = errorMessage == null && guestCount.isNotEmpty() && !isSubmitting,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Submit Booking")
            }
        }
    }
}