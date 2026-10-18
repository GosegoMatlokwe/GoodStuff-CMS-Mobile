package za.co.goodstuffcatering.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import za.co.goodstuffcatering.data.local.MenuItemEntity
import za.co.goodstuffcatering.ui.theme.CreamBackground
import za.co.goodstuffcatering.ui.theme.ForestGreen
import za.co.goodstuffcatering.ui.theme.GoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuDiscoveryScreen(
    navController: NavController,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val menuItems by viewModel.menuItems.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }

    Scaffold(
        containerColor = CreamBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("MENU DISCOVERY", color = GoldAccent, fontSize = 10.sp, letterSpacing = 2.sp)
                        Text("Our Menu.", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                    }
                },
                // FIXED: Added a back button so the user isn't trapped here
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = ForestGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Category Filter Chips
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val categories = listOf("All", "Weddings", "Corporate", "Vegan")
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ForestGreen,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            // Menu List
            LazyColumn(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Filter items based on the selected category
                val filteredItems = if (selectedCategory == "All") {
                    menuItems
                } else {
                    menuItems.filter { it.category == selectedCategory }
                }

                items(filteredItems) { item ->
                    MenuItemCard(item = item)
                }
            }
        }
    }
}

@Composable
fun MenuItemCard(item: MenuItemEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image Placeholder (Using emoji)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(CreamBackground, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("🍽️", fontSize = 32.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Item Details
            Column(modifier = Modifier.weight(1f)) {
                Text(item.itemName, fontWeight = FontWeight.Bold, color = ForestGreen, fontSize = 18.sp)
                Text("R ${item.price} / head", color = GoldAccent, fontWeight = FontWeight.Medium)
            }

            // Add Button
            Button(
                onClick = { /* TODO: Add to booking selection logic */ },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
            ) {
                Text("+ Add", color = Color.White)
            }
        }
    }
}