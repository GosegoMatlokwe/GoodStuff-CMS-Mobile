package za.co.goodstuffcatering.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Define the Dark Mode colors (You can change these to match your brand later)
private val DarkColorScheme = darkColorScheme(
    primary = ForestGreen,
    secondary = GoldAccent,
    tertiary = SoftGreen,
    background = DarkText // Dark background for dark mode
)

// Define the Light Mode colors using your custom brand colors!
private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,           // Used for buttons, active text, etc.
    secondary = GoldAccent,          // Used for accents
    tertiary = SoftGreen,            // Used for subtle backgrounds
    background = CreamBackground,    // The main app background
    onBackground = DarkText,         // Default text color
    surface = CreamBackground        // Card backgrounds
)

@Composable
fun GoodStuffCateringTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Automatically follow phone settings
    dynamicColor: Boolean = false, // Set to false so it uses YOUR colors, not the phone's wallpaper colors
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        // If dynamic color is true and phone is Android 12+, use wallpaper colors
        dynamicColor && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Apply the color scheme and typography to the entire app
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}