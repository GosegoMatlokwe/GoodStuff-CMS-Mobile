package za.co.goodstuffcatering

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import za.co.goodstuffcatering.ui.navigation.AppNavigation
import za.co.goodstuffcatering.ui.theme.GoodStuffCateringTheme
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- FIREBASE ANALYTICS (Moved here so it only runs ONCE when app opens) ---
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle().apply {
            putString("app_open", "App launched successfully")
        }
        analytics.logEvent("app_lifecycle", bundle)

        // Initialize Crashlytics
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        setContent {
            GoodStuffCateringTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}