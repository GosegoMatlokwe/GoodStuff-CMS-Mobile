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

    val menuItems: StateFlow<List<MenuItemEntity>> = bookingDao.getMenuItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _syncState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val syncState: StateFlow<UiState<Unit>> = _syncState

    // Track selected menu items for the booking
    private val _selectedItems = MutableStateFlow<Set<MenuItemEntity>>(emptySet())
    val selectedItems: StateFlow<Set<MenuItemEntity>> = _selectedItems

    init {
        syncMenu()
    }

    fun toggleSelection(item: MenuItemEntity) {
        val current = _selectedItems.value.toMutableSet()
        if (current.contains(item)) {
            current.remove(item)
        } else {
            current.add(item)
        }
        _selectedItems.value = current
    }

    fun syncMenu() {
        viewModelScope.launch {
            _syncState.value = UiState.Loading
            try {
                val response = apiService.getMenuItems()
                response.body()?.let { menuDtos ->
                    val entities = menuDtos.mapIndexed { index, dto ->
                        MenuItemEntity(
                            id = dto.id,
                            itemName = dto.itemName,
                            price = dto.price,
                            // MOCK CATEGORIES: Assign different categories based on ID for demonstration
                            category = when (dto.id % 3) {
                                0 -> "Weddings"
                                1 -> "Corporate"
                                else -> "Vegan"
                            },
                            isAvailable = true
                        )
                    }
                    bookingDao.insertMenuItems(entities)
                    _syncState.value = UiState.Success(Unit)
                } ?: run {
                    _syncState.value = UiState.Error("Server returned empty menu")
                }
            } catch (e: Exception) {
                _syncState.value = UiState.Error("Offline mode: showing cached menu")
            }
        }
    }
}