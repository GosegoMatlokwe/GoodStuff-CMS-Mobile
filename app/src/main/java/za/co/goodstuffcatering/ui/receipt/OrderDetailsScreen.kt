package za.co.goodstuffcatering.ui.receipt

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import za.co.goodstuffcatering.ui.navigation.Screen
import za.co.goodstuffcatering.ui.theme.CreamBackground
import za.co.goodstuffcatering.ui.theme.ForestGreen
import za.co.goodstuffcatering.utils.WhatsAppHelper

@Composable
fun OrderDetailsScreen(
    navController: NavController,
    bookingId: Int, // NEW: Passed from Navigation
    viewModel: OrderManagementViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val receiptFile by viewModel.generatedReceipt.collectAsState()
    val booking by viewModel.booking.collectAsState()

    // Fetch the real booking when the screen loads
    LaunchedEffect(bookingId) {
        viewModel.loadBooking(bookingId)
    }

    // Handle opening the PDF when generated
    LaunchedEffect(receiptFile) {
        receiptFile?.let { file ->
            try {
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_SUBJECT, "Good Stuff Catering Receipt - INV-$bookingId")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, "Share Receipt via"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(CreamBackground).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Display the real booking data safely
        if (booking == null) {
            CircularProgressIndicator(color = ForestGreen)
        } else {
            Text("ORDER #INV-${booking?.id}", color = Color.Gray, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Text(booking?.eventType ?: "", fontSize = 32.sp, color = ForestGreen, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("${booking?.guestCount} Guests", fontSize = 16.sp, color = Color.Gray)
            Text("Date: ${booking?.eventDate}", fontSize = 16.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            Surface(color = Color(0xFFE8F0EC), shape = RoundedCornerShape(20.dp)) {
                Text(
                    booking?.status?.uppercase() ?: "PENDING",
                    color = ForestGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Share PDF Button
            Button(
                onClick = { viewModel.generateReceiptForOrder(context) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
            ) {
                Text("🧾 Share PDF Receipt", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Modify Booking Button
            Button(
                onClick = { navController.navigate(Screen.BookingForm.route) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC5A880))
            ) {
                Text("✏️ Modify Booking", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // WhatsApp Button
            OutlinedButton(
                onClick = {
                    WhatsAppHelper.openWhatsApp(
                        context = context,
                        phoneNumber = "27670263074",
                        message = "Hi Good Stuff Catering, I'd like to confirm details for Order #INV-${booking?.id}."
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, ForestGreen)
            ) {
                Text("💬 Chat on WhatsApp", color = ForestGreen, fontWeight = FontWeight.Bold)
            }
        }
    }
}