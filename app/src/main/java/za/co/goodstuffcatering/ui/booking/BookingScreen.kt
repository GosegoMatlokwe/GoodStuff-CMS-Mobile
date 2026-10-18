package za.co.goodstuffcatering.ui.booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape // <--- FIXED: Added RoundedCornerShape import
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // <--- FIXED: Added Color import
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import za.co.goodstuffcatering.ui.navigation.Screen
import za.co.goodstuffcatering.ui.theme.ForestGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
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
    // Default to today's date formatted correctly
    var eventDate by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())) }

    // State for the Date Picker popup
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(submitSuccess) {
        if (submitSuccess) {
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.BookingForm.route) { inclusive = true }
            }
        }
    }

    // Render the Date Picker if showDatePicker is true
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    // Convert selected date to YYYY-MM-DD format for Django
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        eventDate = formatter.format(Date(millis))
                    }
                }) { Text("OK", color = ForestGreen) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel", color = ForestGreen) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("New Catering Booking", style = MaterialTheme.typography.headlineMedium, color = ForestGreen)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = eventType,
            onValueChange = { eventType = it },
            label = { Text("Event Type") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForestGreen)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Read-only text field that opens the date picker
        OutlinedTextField(
            value = eventDate,
            onValueChange = { }, // Prevents manual typing
            readOnly = true,
            label = { Text("Event Date (YYYY-MM-DD)") },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Select Date", tint = ForestGreen)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForestGreen)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = guestCount,
            onValueChange = { viewModel.onGuestCountChanged(it) },
            label = { Text("Guest Count") },
            isError = errorMessage != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForestGreen)
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
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Submit Booking", color = Color.White)
            }
        }
    }
}