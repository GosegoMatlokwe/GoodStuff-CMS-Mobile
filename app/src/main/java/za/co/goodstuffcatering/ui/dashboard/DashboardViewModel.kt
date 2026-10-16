package za.co.goodstuffcatering.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import za.co.goodstuffcatering.data.remote.dto.BookingResponse
import za.co.goodstuffcatering.data.repository.BookingRepository
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _bookings = MutableStateFlow<List<BookingResponse>>(emptyList())
    val bookings: StateFlow<List<BookingResponse>> = _bookings

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadBookings()
    }

    fun loadBookings() {
        viewModelScope.launch {
            _isLoading.value = true
            val token = FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.await()?.token
            if (token != null) {
                val result = bookingRepository.getBookings(token)
                if (result.isSuccess) {
                    _bookings.value = result.getOrNull() ?: emptyList()
                }
            }
            _isLoading.value = false
        }
    }
}