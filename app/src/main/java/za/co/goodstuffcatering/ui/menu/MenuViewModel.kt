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

    // Read from Room (Offline-First Strategy)
    // stateIn converts the Flow into a StateFlow so the UI can easily observe it.
    // WhileSubscribed(5000) keeps the data alive for 5 seconds if the screen rotates.
    val menuItems: StateFlow<List<MenuItemEntity>> = bookingDao.getMenuItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Tracks the status of the network sync
    private val _syncState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val syncState: StateFlow<UiState<Unit>> = _syncState

    init {
        // Automatically fetch new data when the ViewModel is created
        syncMenu()
    }

    fun syncMenu() {
        viewModelScope.launch {
            _syncState.value = UiState.Loading
            try {
                // API automatically gets the token via AuthInterceptor
                val response = apiService.getMenuItems()

                // CRITICAL FIX: Removed the '!!' (double bang) operator.
                // We use '?.let' to safely handle the response. If body() is null, it skips this block.
                response.body()?.let { menuDtos ->
                    val entities = menuDtos.map { dto ->
                        MenuItemEntity(
                            id = dto.id,
                            itemName = dto.itemName,
                            price = dto.price,
                            category = "General", // Default category
                            isAvailable = true
                        )
                    }
                    // Save the new list to Room. The UI will automatically update!
                    bookingDao.insertMenuItems(entities)
                    _syncState.value = UiState.Success(Unit)
                } ?: run {
                    // This runs if response.body() was null
                    _syncState.value = UiState.Error("Server returned empty menu")
                }
            } catch (e: Exception) {
                // If network fails, the UI will still show the cached data from Room
                _syncState.value = UiState.Error("Offline mode: showing cached menu")
            }
        }
    }
}