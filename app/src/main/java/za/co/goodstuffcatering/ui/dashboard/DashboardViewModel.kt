package za.co.goodstuffcatering.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import za.co.goodstuffcatering.data.local.BookingEntity
import za.co.goodstuffcatering.data.repository.BookingRepository
import za.co.goodstuffcatering.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<BookingEntity>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<BookingEntity>>> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            bookingRepository.cachedBookings.collectLatest { localBookings ->
                // If there is local data, show it immediately
                if (localBookings.isNotEmpty()) {
                    _uiState.value = UiState.Success(localBookings)
                }
            }
        }
        syncData()
    }

    fun syncData() {
        viewModelScope.launch {
            if (_uiState.value !is UiState.Success) {
                _uiState.value = UiState.Loading
            }

            val result = bookingRepository.syncBookingsFromNetwork()

            if (result.isFailure) {
                if (_uiState.value !is UiState.Success) {
                    _uiState.value = UiState.Error(
                        message = "Could not load bookings. Check your connection.",
                        retryAction = { syncData() }
                    )
                }
            }
        }
    }



    // NEW: Function called by the DashboardScreen when the user confirms deletion
    fun deleteBooking(bookingId: Int) {
        viewModelScope.launch {
            val result = bookingRepository.deleteBooking(bookingId)
            if (result.isFailure) {
                // In a production app, you would show a Toast or Snackbar here
                // e.g., "Failed to delete. Check your internet connection."
            }
        }
    }


}