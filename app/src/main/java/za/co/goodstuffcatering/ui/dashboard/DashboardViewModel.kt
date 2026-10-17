package za.co.goodstuffcatering.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import za.co.goodstuffcatering.data.local.BookingEntity
import za.co.goodstuffcatering.data.repository.BookingRepository
import za.co.goodstuffcatering.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    // UI state for the screen
    private val _uiState = MutableStateFlow<UiState<List<BookingEntity>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<BookingEntity>>> = _uiState.asStateFlow()

    init {
        // Collect from Room DB (Offline-First)
        viewModelScope.launch {
            bookingRepository.cachedBookings.collect { localBookings ->
                if (localBookings.isNotEmpty()) {
                    _uiState.value = UiState.Success(localBookings)
                }
            }
        }
        // Trigger network sync
        syncData()
    }

    fun syncData() {
        viewModelScope.launch {
            if (_uiState.value !is UiState.Success) {
                _uiState.value = UiState.Loading
            }

            val token = FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.await()?.token
            if (token != null) {
                val result = bookingRepository.syncBookingsFromNetwork(token)
                if (result.isFailure && _uiState.value !is UiState.Success) {
                    // If network fails AND we have no cache, show Error with Retry
                    _uiState.value = UiState.Error(
                        message = "Could not load bookings. Check your internet.",
                        retryAction = { syncData() }
                    )
                }
            } else {
                _uiState.value = UiState.Error("User not authenticated")
            }
        }
    }
}
