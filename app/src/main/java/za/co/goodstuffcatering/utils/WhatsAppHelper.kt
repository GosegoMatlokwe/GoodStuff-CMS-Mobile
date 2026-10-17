package za.co.goodstuffcatering.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object WhatsAppHelper {
    /**
     * Opens WhatsApp chat with the business.
     * @param phoneNumber The business number (with country code, e.g., "27686316259")
     */
    fun openWhatsApp(context: Context, phoneNumber: String, message: String) {
        try {
            val uri = Uri.parse("https://wa.me/$phoneNumber?text=${Uri.encode(message)}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.whatsapp") // Force open in WhatsApp
            context.startActivity(intent)
        } catch (e: Exception) {
            // WhatsApp not installed, open in browser
            val uri = Uri.parse("https://wa.me/$phoneNumber?text=${Uri.encode(message)}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }
    }
}