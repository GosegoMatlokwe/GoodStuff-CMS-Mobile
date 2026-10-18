package za.co.goodstuffcatering.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
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
import za.co.goodstuffcatering.ui.common.UiState
import za.co.goodstuffcatering.ui.navigation.Screen
import za.co.goodstuffcatering.ui.theme.CreamBackground
import za.co.goodstuffcatering.ui.theme.ForestGreen
import za.co.goodstuffcatering.ui.theme.GoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val firstName = currentUser?.displayName?.split(" ")?.firstOrNull() ?: "there"

    Scaffold(
        containerColor = CreamBackground,
        topBar = {
            TopAppBar(
                title = { Text("Dashboard", color = ForestGreen, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground),
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = ForestGreen)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                // If they click FAB, default price is 150.0
                onClick = { navController.navigate(Screen.BookingForm.createRoute(150.0)) },
                containerColor = ForestGreen,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Booking", tint = Color.White)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 16.dp)) {

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Good morning,", fontSize = 16.sp, color = Color.Gray)
            Text(text = "$firstName.", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
            Spacer(modifier = Modifier.height(24.dp))

            // Browse Menu Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clickable { navController.navigate(Screen.Menu.route) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ForestGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Hungry?", color = GoldAccent, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Browse our premium catering menu.", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Text("🍽️", fontSize = 40.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Upcoming Bookings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreen,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when (val state = uiState) {
                    is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = ForestGreen)
                    is UiState.Error -> {
                        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = state.message, color = MaterialTheme.colorScheme.error)
                            Button(onClick = { state.retryAction?.invoke() }, colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)) {
                                Text("Retry")
                            }
                        }
                    }
                    is UiState.Success -> {
                        if (state.data.isEmpty()) {
                            Text("No upcoming bookings. Tap + to create one!", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                        } else {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(state.data) { booking ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Box(
                                                        modifier = Modifier.size(40.dp).background(CreamBackground, RoundedCornerShape(8.dp)),
                                                        contentAlignment = Alignment.Center
                                                    ) { Text("📅", fontSize = 20.sp) }

                                                    Spacer(modifier = Modifier.width(12.dp))

                                                    Column {
                                                        Text(booking.eventType, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ForestGreen)
                                                        Text(booking.eventDate, color = Color.Gray, fontSize = 14.sp)
                                                    }
                                                }

                                                Surface(
                                                    color = if (booking.status == "Confirmed") Color(0xFFE8F0EC) else Color(0xFFFFF3CD),
                                                    shape = RoundedCornerShape(8.dp)
                                                ) {
                                                    Text(
                                                        text = booking.status,
                                                        color = if (booking.status == "Confirmed") ForestGreen else Color(0xFF856404),
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 12.sp,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(16.dp))
                                            Divider(color = Color.LightGray.copy(alpha = 0.5f))
                                            Spacer(modifier = Modifier.height(8.dp))

                                            // YOUR REQUESTED SNIPPET
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                OutlinedButton(
                                                    onClick = { navController.navigate(Screen.OrderDetails.createRoute(booking.id)) },
                                                    shape = RoundedCornerShape(50),
                                                    border = BorderStroke(1.dp, ForestGreen),
                                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                                                    modifier = Modifier.height(32.dp)
                                                ) {
                                                    Text("View Receipt", fontSize = 12.sp, color = ForestGreen)
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
        }
    }
}