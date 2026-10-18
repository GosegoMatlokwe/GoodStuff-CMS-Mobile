package za.co.goodstuffcatering.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
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
    val updateState by viewModel.updateState.collectAsState()
    val currentUser = FirebaseAuth.getInstance().currentUser

    // Profile State (Pre-fill with Firebase data)
    val fullName = currentUser?.displayName ?: ""
    val nameParts = fullName.split(" ")
    var firstName by remember { mutableStateOf(nameParts.getOrNull(0) ?: "") }
    var lastName by remember { mutableStateOf(nameParts.getOrNull(1) ?: "") }
    var phone by remember { mutableStateOf("") } // Phone starts empty (will be pulled from DB later if needed)
    var showPopiaDialog by remember { mutableStateOf(false) }

    var darkMode by remember { mutableStateOf(false) }
    var pushNotifications by remember { mutableStateOf(true) }

    // Show success/error messages via AlertDialogs
    if (updateState is UpdateState.Success) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Success") },
            text = { Text("Profile updated successfully!") },
            confirmButton = { TextButton(onClick = { /* Reset state if needed */ }) { Text("OK") } }
        )
    }

    LaunchedEffect(deleteState) {
        if (deleteState is DeleteState.Success) {
            navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } }
        }
    }

    // --- POPIA DIALOG ---
    if (showPopiaDialog) {
        AlertDialog(
            onDismissRequest = { showPopiaDialog = false },
            title = { Text("Privacy Policy & POPIA", fontWeight = FontWeight.Bold, color = ForestGreen) },
            text = {
                // Scrollable column for the long text
                Column(modifier = Modifier.heightIn(max = 400.dp).verticalScroll(rememberScrollState())) {
                    Text(
                        text = """
                            Privacy Policy & POPIA Consent Notice
                            Effective Date: October 18, 2026
                            Company: GoodStuff Catering
                            
                            1. Introduction
                            GoodStuff Catering ("we", "us", or "our") is committed to protecting your privacy and ensuring that your personal information is handled in a safe and responsible manner in accordance with the Protection of Personal Information Act 4 of 2013 (POPIA). This Privacy Policy outlines how we collect, use, process, and protect your data when you interact with our mobile application, website, and catering services.
                            
                            2. Information We Collect
                            To provide you with our catering and event management services, we collect the following categories of personal information:
                            - Identity & Contact Data: Full name, email address, phone number, and company name.
                            - Delivery & Location Data: Physical addresses for event hosting.
                            - Special Personal Information: Dietary requirements, food allergies. Under POPIA, this requires your explicit consent.
                            - Financial & Transaction Data: Payment methods, invoice records. We do not store raw credit card numbers.
                            - System Data: App usage data, Firebase authentication tokens.
                            
                            3. Purpose of Data Collection
                            We use your personal information to process bookings, ensure meals adhere to dietary restrictions, process payments, and communicate regarding your booking status.
                            
                            4. Data Sharing
                            We do not sell or rent your data. We share data only with payment processors, delivery logistics, and cloud infrastructure providers.
                            
                            5. Data Security & Retention
                            We implement industry-standard security measures. We retain data only as long as necessary or as mandated by law.
                            
                            6. Your Rights Under POPIA
                            You have the right to request access, correction/deletion, object to processing, and withdraw consent at any time.
                            
                            7. Contact Information
                            Email: privacy@goodstuffcatering.co.za
                        """.trimIndent(),
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = { TextButton(onClick = { showPopiaDialog = false }) { Text("Close") } }
        )
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
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- EDITABLE PROFILE CARD ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(50.dp).background(ForestGreen, CircleShape), contentAlignment = Alignment.Center) {
                            Text(fullName.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Your Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ForestGreen)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = firstName, onValueChange = { firstName = it },
                        label = { Text("Name") }, modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForestGreen)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = lastName, onValueChange = { lastName = it },
                        label = { Text("Surname") }, modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForestGreen)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = currentUser?.email ?: "", onValueChange = { },
                        label = { Text("Email") }, modifier = Modifier.fillMaxWidth(),
                        readOnly = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForestGreen)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = phone, onValueChange = { phone = it },
                        label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForestGreen)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.updateProfile(firstName, lastName, phone) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        enabled = updateState !is UpdateState.Loading
                    ) {
                        if (updateState is UpdateState.Loading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                        else Text("Save Profile", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- PREFERENCES & POPIA LINK ---
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
                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray)

                    // POPIA Clickable Text
                    Text(
                        text = "View Privacy Policy & POPIA",
                        color = ForestGreen,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.fillMaxWidth().clickable { showPopiaDialog = true }.padding(vertical = 8.dp)
                    )
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
                        if (deleteState is DeleteState.Loading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                        else Text("Delete Account (POPIA)", color = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}