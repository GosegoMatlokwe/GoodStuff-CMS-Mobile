package za.co.goodstuffcatering

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
        setContent {
            GoodStuffCateringTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)

                    val analytics = FirebaseAnalytics.getInstance(this)
                    val crashlytics = FirebaseCrashlytics.getInstance()

                    // Track a custom event (for drop-off analysis)
                    val bundle = Bundle().apply {
                        putString("booking_stage", "guest_count_entered")
                    }
                    analytics.logEvent("booking_funnel", bundle)
                    try {
                        // API call
                    } catch (e: Exception) {
                        crashlytics.recordException(e)
                    }


                }

                }

        }
    }
}