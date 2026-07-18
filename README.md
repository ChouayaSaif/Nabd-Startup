# Nabd (نبض) - Healthcare Adherence Platform

**Nabd** is a premium, AI-driven healthcare application designed to manage medication adherence in Tunisia. It bridges the gap between healthcare professionals and patients through a high-fidelity mobile portal and physical 'PillMate' device integration.


## Key Features

### 1. Advanced Physician Portal
- **Patient Dashboard:** A searchable, real-time list of patients under care, showing hardware connection status.
- **AI Analytics & Insights:** 
    - **Risk Gauge:** Predictive visualization of patient risk using color-coded radial charts.
    - **Trend Analysis:** 30-day compliance graphs showing habit improvements.
    - **AI-Driven Insights:** Automated flagging of high-priority missed doses and optimized nudge times.

### 2. Medication & Device Management
- **Long-Form Configuration:** Detailed medication scheduling (Morning, Afternoon, Evening) with dosage controls.
- **PillMate Pairing Hub:** Seamless hardware linking via manual entry or QR code scanning.
- **Hardware Sync:** Direct synchronization of clinical schedules to physical devices.

### 3. Secure Authentication
- **Role-Based Flow:** Distinct registration paths for Patients and Healthcare Professionals.
- **Firebase Integration:** Secure Sign In and Sign Up powered by Google Firebase.
- **Google Sign-In:** Configured for high-friction-less onboarding.

## Design System
Nabd features a **"Premium Medical"** aesthetic:
- **Primary (Heart/Vitality):** `#bd192e` (Deep Red)
- **Secondary (Clarity/Navy):** `#0a2e45`
- **Background:** `#fbf9ea` (Cream/White)
- **Typography:** Modern, accessible sans-serif font scaling.

## Tech Stack
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material Design 3)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Navigation:** Jetpack Navigation Compose
- **Backend:** Firebase (Authentication, Analytics)
- **Visuals:** Custom Canvas drawings for gauges and graphs.

## Getting Started
1.  **Clone the project** into Android Studio.
2.  **Sync Gradle:** Ensure you have the `google-services.json` file in the `app/` directory.
3.  **Run:** Build and run on an Android 12+ device (to see the customized System Splash Screen).

---
*Designed for Medical Excellence in Tunisia.*
