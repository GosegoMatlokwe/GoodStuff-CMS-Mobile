package za.co.goodstuffcatering.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll // <--- NEW IMPORT
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
import com.google.firebase.auth.FirebaseAuth
import za.co.goodstuffcatering.ui.navigation.Screen
import za.co.goodstuffcatering.ui.theme.CreamBackground
import za.co.goodstuffcatering.ui.theme.ForestGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val deleteState by viewModel.deleteState.collectAsState()
    val currentUser = FirebaseAuth.getInstance().currentUser

    var darkMode by remember { mutableStateOf(false) }
    var pushNotifications by remember { mutableStateOf(true) }

    LaunchedEffect(deleteState) {
        if (deleteState is DeleteState.Success) {
            navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } }
        }
    }

    Scaffold(
        containerColor = CreamBackground,
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = ForestGreen, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()) // <--- FIXED: Added vertical scroll
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- PROFILE CARD ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(50.dp).background(ForestGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            val fullName = currentUser?.displayName ?: "Guest User"
                            Text(fullName.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Your Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ForestGreen)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = currentUser?.displayName ?: "", onValueChange = {},
                        label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(),
                        readOnly = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForestGreen)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = currentUser?.email ?: "", onValueChange = {},
                        label = { Text("Email") }, modifier = Modifier.fillMaxWidth(),
                        readOnly = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForestGreen)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- PREFERENCES ---
            Text("Preferences", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.Bold, color = ForestGreen)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Dark Mode", fontWeight = FontWeight.Medium)
                        Switch(checked = darkMode, onCheckedChange = { darkMode = it }, colors = SwitchDefaults.colors(checkedThumbColor = ForestGreen))
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Push Notifications", fontWeight = FontWeight.Medium)
                        Switch(checked = pushNotifications, onCheckedChange = { pushNotifications = it }, colors = SwitchDefaults.colors(checkedThumbColor = ForestGreen))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- DANGER ZONE ---
            Text("Danger Zone", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.Bold, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Deleting your account is permanent and will erase all your bookings.", color = Color.DarkGray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.deleteAccount() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        enabled = deleteState !is DeleteState.Loading,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (deleteState is DeleteState.Loading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                        } else {
                            Text("Delete Account (POPIA)", color = Color.White)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp)) // Bottom padding
        }
    }
}