# GoodStuff-CMS-Mobile
# Good Stuff Catering - Android App

## Overview
A Kotlin Android application for Good Stuff Catering to manage bookings, menus, and staff schedules. Built with Jetpack Compose, MVVM, Hilt, Retrofit, and Firebase Auth.

## Setup Instructions
1. Clone the repository.
2. Open in Android Studio (Hedgehog or later).
3. Add your `google-services.json` from Firebase to the `app/` directory.
4. Update the `BASE_URL` in `NetworkModule.kt` with your Django API URL.
5. Sync Gradle and run on a physical Android device.

## Features
- **SSO Login:** Google Sign-In via Firebase.
- **Dashboard:** View upcoming bookings from the Django API.
- **Booking:** Create new bookings with input validation.
- **Settings:** Delete account (POPIA compliance).

## API Endpoints
- `POST /auth/sync/` - Sync Firebase user with Django.
- `GET /bookings/` - Fetch user bookings.
- `POST /bookings/create/` - Create a new booking.
- `DELETE /auth/delete-account/` - Delete user account.

## Testing
Run unit tests via `./gradlew testDebugUnitTest`.  
GitHub Actions automates build and test on every push.

## Demonstration Video
[Link to be added]
