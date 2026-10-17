package za.co.goodstuffcatering.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import za.co.goodstuffcatering.data.local.BookingDao
import za.co.goodstuffcatering.data.local.MenuItemEntity
import za.co.goodstuffcatering.data.remote.ApiService
import za.co.goodstuffcatering.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val bookingDao: BookingDao,
    private val apiService: ApiService
) : ViewModel() {

    // Read from Room (Offline-First)
    val menuItems: StateFlow<List<MenuItemEntity>> = bookingDao.getMenuItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _syncState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val syncState: StateFlow<UiState<Unit>> = _syncState

    init {
        syncMenu()
    }

    fun syncMenu() {
        viewModelScope.launch {
            _syncState.value = UiState.Loading
            try {
                // API automatically gets token via AuthInterceptor
                val response = apiService.getMenuItems()
                if (response.isSuccessful && response.body() != null) {
                    val entities = response.body()!!.map { dto ->
                        MenuItemEntity(
                            id = dto.id,
                            itemName = dto.itemName,
                            price = dto.price,
                            category = "General", // Update if Django sends categories
                            isAvailable = true
                        )
                    }
                    bookingDao.insertMenuItems(entities)
                    _syncState.value = UiState.Success(Unit)
                } else {
                    _syncState.value = UiState.Error("Failed to load menu")
                }
            } catch (e: Exception) {
                // If network fails, UI still shows cached menu from Room
                _syncState.value = UiState.Error("Offline mode: showing cached menu")
            }
        }
    }
}