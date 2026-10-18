package za.co.goodstuffcatering.data.local

import android.content.Context
// Imports for AndroidX Security Crypto library
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
// Imports for Hilt Dependency Injection
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @Singleton ensures there is only ONE instance of this class in the entire app.
 * @Inject constructor allows Hilt to provide the Context automatically.
 */
@Singleton
class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    // MasterKey is the key used to encrypt and decrypt the data.
    // AES256_GCM is a highly secure, industry-standard encryption algorithm.
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // Create the EncryptedSharedPreferences. This works like normal SharedPreferences,
    // but the keys and values are securely encrypted on the device's disk.
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_prefs", // The name of the file where data is stored
        masterKey,       // The encryption key
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // Encrypts the keys
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // Encrypts the values
    )

    // Saves the JWT token securely.
    fun saveToken(token: String) {
        // .apply() saves the data asynchronously (in the background) so it doesn't block the UI.
        sharedPreferences.edit().putString("jwt_token", token).apply()
    }

    // Retrieves the JWT token. Returns null if no token has been saved yet.
    fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    // Deletes the token (used when the user logs out or deletes their account for POPIA compliance).
    fun clearToken() {
        sharedPreferences.edit().remove("jwt_token").apply()
    }
}