package za.co.goodstuffcatering.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import za.co.goodstuffcatering.ui.navigation.Screen
import za.co.goodstuffcatering.ui.theme.CreamBackground
import za.co.goodstuffcatering.ui.theme.ForestGreen
import za.co.goodstuffcatering.ui.theme.GoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    // Collect states from the ViewModel
    val phone by viewModel.phoneNumber.collectAsState()
    val consent by viewModel.popiaConsent.collectAsState()
    val state by viewModel.registerState.collectAsState()

    // If registration is successful, navigate to the Dashboard
    LaunchedEffect(state) {
        if (state is RegisterState.Success) {
            navController.navigate(Screen.Dashboard.route) {
                // Clear the backstack so they can't go back to registration
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        containerColor = CreamBackground,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("< Back", color = ForestGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Header Text
            Text(
                text = "WELCOME TO",
                color = GoldAccent,
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Complete your\nprofile.",
                fontSize = 36.sp,
                color = ForestGreen,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "We need a few more details to secure your account.",
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Phone Number Input
            OutlinedTextField(
                value = phone,
                onValueChange = viewModel::onPhoneChanged,
                label = { Text("Phone Number (e.g. 0821234567)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ForestGreen,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // POPIA Consent Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    // Makes the entire row clickable, not just the tiny checkbox
                    .clickable { viewModel.onConsentChanged(!consent) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(
                    checked = consent,
                    onCheckedChange = { viewModel.onConsentChanged(it) },
                    colors = CheckboxDefaults.colors(checkedColor = ForestGreen)
                )
                Text(
                    text = "I consent to the processing of my personal data in accordance with POPIA.",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 12.dp, start = 8.dp)
                )
            }

            // Display error message if validation fails
            if (state is RegisterState.Error) {
                Text(
                    text = (state as RegisterState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Pushes the button to the bottom of the screen
            Spacer(modifier = Modifier.weight(1f))

            // Submit Button
            Button(
                onClick = { viewModel.submitRegistration() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                // Disable the button if loading OR if they haven't ticked the POPIA box
                enabled = state !is RegisterState.Loading && consent
            ) {
                if (state is RegisterState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("COMPLETE REGISTRATION", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}