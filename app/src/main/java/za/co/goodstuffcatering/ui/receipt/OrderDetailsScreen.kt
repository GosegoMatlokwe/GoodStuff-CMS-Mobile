package za.co.goodstuffcatering.ui.receipt

import android.content.Intent
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
import za.co.goodstuffcatering.data.local.OrderEntity
import za.co.goodstuffcatering.ui.theme.CreamBackground
import za.co.goodstuffcatering.ui.theme.ForestGreen
import za.co.goodstuffcatering.utils.WhatsAppHelper
import androidx.compose.foundation.BorderStroke

@Composable
fun OrderDetailsScreen(
    navController: NavController,
    viewModel: OrderManagementViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val receiptFile by viewModel.generatedReceipt.collectAsState()

    // Creating a mock order for testing purposes
    val mockOrder = OrderEntity(
        orderId = 1,
        serverId = "INV-12345",
        eventDate = "2026/06/05",
        eventType = "Wedding",
        guestCount = 150,
        venueAddress = "Fourways, JHB",
        deliveryFee = 25.0,
        totalCost = 29353.75,
        status = "OUT FOR DELIVERY"
    )

    // Handle opening the PDF when generated
    LaunchedEffect(receiptFile) {
        receiptFile?.let { file ->
            try {
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/pdf")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text("ORDER #12345", color = Color.Gray, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
        Text("Wedding", fontSize = 32.sp, color = ForestGreen, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        // Status Pill
        Surface(
            color = Color(0xFFE8F0EC),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                "OUT FOR DELIVERY",
                color = ForestGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // View PDF Button
        Button(
            onClick = {
                // FIXED: Correctly passing the mock order to the ViewModel
                viewModel.generateReceiptForOrder(context, mockOrder)
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
        ) {
            Text("🧾 View PDF Receipt", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // WhatsApp Button
        OutlinedButton(
            onClick = {
                WhatsAppHelper.openWhatsApp(
                    context = context,
                    phoneNumber = "27686316259",
                    message = "Hi Good Stuff Catering, I'd like to confirm details for Order #12345."
                )
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(28.dp),
            border = BorderStroke(1.dp, ForestGreen)
        ) {
            Text("💬 Contact Coordinator on WhatsApp", color = ForestGreen, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            "Need to make changes? You can adjust guest counts until 3 days before the event.",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}