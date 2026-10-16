package za.co.goodstuffcatering

import org.junit.Assert.assertEquals
import org.junit.Test
import za.co.goodstuffcatering.ui.booking.BookingViewModel

class BookingValidationTest {

    @Test
    fun `test invalid guest count shows error`() {
        val viewModel = BookingViewModel()

        // Test input less than 10
        viewModel.onGuestCountChanged("5")
        assertEquals("Minimum booking is 10 guests for catering", viewModel.errorMessage.value)

        // Test non-numeric input
        viewModel.onGuestCountChanged("abc")
        assertEquals("Please enter a valid number", viewModel.errorMessage.value)

        // Test valid input
        viewModel.onGuestCountChanged("50")
        assertEquals(null, viewModel.errorMessage.value)
    }
}