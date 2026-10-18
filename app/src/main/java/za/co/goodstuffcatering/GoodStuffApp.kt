package za.co.goodstuffcatering

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// @HiltAndroidApp is CRITICAL. Without this, the app will crash immediately
// because Hilt doesn't know how to create the dependency container.
@HiltAndroidApp
class GoodStuffApp : Application() {
}