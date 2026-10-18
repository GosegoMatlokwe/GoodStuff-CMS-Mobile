package za.co.goodstuffcatering.ui.common

/**
 * A generic sealed class used to represent the different states of a UI.
 * 'out T' means it's covariant, allowing us to use subclasses (like List<BookingEntity>) safely.
 * This prevents the app from crashing by forcing the UI to handle Loading, Success, and Error explicitly.
 */
sealed class UiState<out T> {
    // Represents the state when data is currently being fetched (shows a spinner)
    object Loading : UiState<Nothing>()

    // Represents the state when data is successfully loaded.
    // It holds the actual data (e.g., a list of bookings).
    data class Success<T>(val data: T) : UiState<T>()

    // Represents the state when an error occurs.
    // It includes a message to show the user, and an optional retryAction function.
    data class Error(val message: String, val retryAction: (() -> Unit)? = null) : UiState<Nothing>()
}