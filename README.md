# Good Stuff Catering - Android Mobile App

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-blue.svg?style=flat&logo=kotlin)
![Android Studio](https://img.shields.io/badge/Android_Studio-Hedgehog-green.svg?style=flat&logo=android-studio)
![Firebase](https://img.shields.io/badge/Firebase-Authentication-orange.svg?style=flat&logo=firebase)
![Django](https://img.shields.io/badge/Backend-Django_REST-darkgreen.svg?style=flat&logo=django)
![GitHub Actions](https://img.shields.io/badge/CI/CD-GitHub_Actions-brightgreen.svg?style=flat&logo=github-actions)

## 📱 Project Overview
The Good Stuff Catering Android app is a premium, offline-first mobile application designed to streamline the event booking process for a high-end catering business. It serves as the customer-facing presentation layer that communicates securely with a decoupled Django REST API backend (hosted on PythonAnywhere) and a relational database.

This project was developed as part of the Work-Integrated Learning (WIL) Portfolio of Evidence (PoE), demonstrating a full-stack enterprise architecture.

## ✨ Core Features
*   **Single Sign-On (SSO):** Secure Google Sign-In powered by Firebase Authentication using the modern Android Credential Manager API.
*   **POPIA Compliance:** Explicit consent tracking with timestamps. Includes a fully functional "Right to Erasure" (Delete Account) feature that permanently wipes user data from the live database.
*   **Offline-First Architecture:** Utilizes **Room (SQLite)** to cache menus and bookings locally. If the user loses connection, they can still view their upcoming events and the menu. Data syncs automatically when the connection is restored.
*   **Dynamic Booking Engine:** Calculates estimated costs based on guest count, menu price per head, and South African VAT (15%).
*   **Menu Discovery:** Browse categorized catering menus with a seamless "Add to Cart" selection flow.
*   **PDF Receipt Generation:** Automatically generates professional invoices with banking details, VAT breakdowns, and event details using Android's native `PdfDocument` API. The receipt can be shared via WhatsApp, Email, or saved to the device.
*   **WhatsApp Integration:** Direct deep-link to the catering coordinator for instant communication.
*   **Push Notifications:** Firebase Cloud Messaging (FCM) configured for booking status updates.

## 🛠️ Tech Stack & Architecture
### Android Frontend
*   **Language:** Kotlin
*   **UI Toolkit:** Jetpack Compose (Material Design 3)
*   **Architecture:** MVVM (Model-View-ViewModel) + Clean Architecture principles
*   **Dependency Injection:** Dagger Hilt
*   **Networking:** Retrofit2 & OkHttp3 (with a custom `AuthInterceptor` for automatic JWT attachment)
*   **Local Database:** Room DB (Offline-first caching)
*   **Authentication:** Firebase Auth (Google SSO)

### Backend & DevOps (Decoupled System)
*   **Backend API:** Python / Django REST Framework (Hosted on PythonAnywhere)
*   **Database:** SQLite/PostgreSQL (Django ORM)
*   **Web Dashboard:** Django Admin customized with Jazzmin Theme
*   **CI/CD Pipeline:** GitHub Actions (Automated Build & Unit Tests on every push to `main`)
*   **Project Management:** Azure DevOps (Kanban Board)

## 🚀 Setup & Installation

### Prerequisites
*   Android Studio (Hedgehog or newer)
*   A physical Android device (API 26+) or Emulator
*   A Google Firebase Account

### Installation Steps
1. **Clone the repository:**
   ```bash
   git clone https://github.com/GosegoMatlokwe/GoodStuff-CMS-Mobile.git

##Demonstration Video: 
[Link here]
