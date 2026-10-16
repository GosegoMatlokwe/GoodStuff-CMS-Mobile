package za.co.goodstuffcatering.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import za.co.goodstuffcatering.ui.navigation.Screen

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val deleteState by viewModel.deleteState.collectAsState()

    LaunchedEffect(deleteState) {
        if (deleteState is DeleteState.Success) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.deleteAccount() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            enabled = deleteState !is DeleteState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (deleteState is DeleteState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Delete Account (POPIA)")
            }
        }

        if (deleteState is DeleteState.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = (deleteState as DeleteState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}