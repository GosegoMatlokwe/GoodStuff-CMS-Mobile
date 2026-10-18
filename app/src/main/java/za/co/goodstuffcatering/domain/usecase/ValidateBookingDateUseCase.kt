package za.co.goodstuffcatering.domain.usecase

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * UseCase: Validates that the event date is in the future.
 */
class ValidateBookingDateUseCase @Inject constructor() {

    /**
     * Checks if the provided date string is valid and in the future.
     * @param dateString The date provided by the user (formatted as YYYY-MM-DD).
     * @return True if the date is in the future, false if it's in the past or invalid.
     */
    fun execute(dateString: String): Boolean {
        return try {
            // Define the expected format (e.g., "2026-06-05")
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            // Parse the string into a LocalDate object
            val selectedDate = LocalDate.parse(dateString, formatter)

            // Get today's date
            val today = LocalDate.now()

            // Return true if the selected date is AFTER today.
            // If it's today or in the past, it returns false.
            selectedDate.isAfter(today)

        } catch (e: Exception) {
            // If the user typed "hello" instead of a date, the parser will throw an error.
            // We catch it and return false so the app doesn't crash.
            false
        }
    }
}