package za.co.goodstuffcatering.ui.booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import za.co.goodstuffcatering.ui.theme.CreamBackground
import za.co.goodstuffcatering.ui.theme.ForestGreen
import za.co.goodstuffcatering.ui.theme.GoldAccent

@Composable
fun QuoteCheckoutScreen(
    navController: NavController,
    viewModel: BookingEngineViewModel = hiltViewModel()
) {
    val quote by viewModel.currentQuote.collectAsState()
    val guests by viewModel.guestCount.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "REVIEW YOUR",
            color = GoldAccent,
            fontSize = 12.sp,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Booking.",
            fontSize = 36.sp,
            color = ForestGreen,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Wedding", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = ForestGreen)
                Text("5 June 2026", color = Color.Gray)
                Text("$guests Guests", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("MENU SELECTED", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))

        // We mock the breakdown here for the UI
        PriceRow(label = "Beef Stew ($guests)", price = "R ${(150 * guests)}.00")
        PriceRow(label = "Garden Salad ($guests)", price = "R ${(20 * guests)}.00")

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        PriceRow(label = "Subtotal", price = "R ${quote?.foodTotal ?: 0.0}")
        PriceRow(label = "Delivery Fee", price = "R ${quote?.deliveryFee ?: 0.0}")
        PriceRow(label = "VAT (15%)", price = "R ${quote?.vatAmount ?: 0.0}")

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("TOTAL", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = ForestGreen)
            Text("R ${quote?.totalCost ?: 0.0}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = ForestGreen)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* Proceed to Peach Payments */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
        ) {
            Text("CONFIRM & PAY DEPOSIT", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PriceRow(label: String, price: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray)
        Text(price, fontWeight = FontWeight.Medium)
    }
}