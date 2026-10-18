package za.co.goodstuffcatering.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import za.co.goodstuffcatering.ui.navigation.Screen
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
    val selectedItems by viewModel.selectedItems.collectAsState()
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
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = ForestGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
            )
        },
        bottomBar = {
            // Show a checkout bar if items are selected
            if (selectedItems.isNotEmpty()) {
                Surface(color = Color.White, shadowElevation = 8.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("${selectedItems.size} items selected", fontWeight = FontWeight.Bold)
                            Text("R ${selectedItems.sumOf { it.price }}", color = ForestGreen, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { navController.navigate(Screen.BookingForm.route) },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Proceed to Booking")
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            // Categories Row
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
                // FIXED: Filtering now works because ViewModel assigns categories
                val filteredItems = if (selectedCategory == "All") {
                    menuItems
                } else {
                    menuItems.filter { it.category == selectedCategory }
                }

                items(filteredItems) { item ->
                    val isSelected = selectedItems.contains(item)
                    MenuItemCard(
                        item = item,
                        isSelected = isSelected,
                        onAddClick = { viewModel.toggleSelection(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun MenuItemCard(
    item: MenuItemEntity,
    isSelected: Boolean,
    onAddClick: () -> Unit
) {
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
            Box(
                modifier = Modifier.size(80.dp).background(CreamBackground, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("🍽️", fontSize = 32.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.itemName, fontWeight = FontWeight.Bold, color = ForestGreen, fontSize = 18.sp)
                Text("R ${item.price} / head", color = GoldAccent, fontWeight = FontWeight.Medium)
            }

            // FIXED: Button now toggles selection
            Button(
                onClick = onAddClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) ForestGreen else Color.LightGray.copy(alpha = 0.3f),
                    contentColor = if (isSelected) Color.White else ForestGreen
                )
            ) {
                if (isSelected) {
                    Icon(Icons.Default.Check, contentDescription = "Added")
                } else {
                    Text("+ Add")
                }
            }
        }
    }
}