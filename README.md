# Nabd (نبض) - Healthcare Adherence Platform

**Nabd** is a premium, AI-driven healthcare application designed to manage medication adherence in Tunisia. It bridges the gap between healthcare professionals and patients through a high-fidelity mobile portal and physical 'PillMate' device integration.

<img width="889" height="280" alt="logo" src="https://github.com/user-attachments/assets/fe7f3151-7aef-441c-8130-c0a2b7a32c5a" />

## User Journey
1. **Authentication:** Patients and Doctors authenticate via a secure, role-based Firebase flow.
2. **Onboarding & Device Pairing:** Users link their PillMate hardware via QR code scanning or manual alphanumeric entry.
3. **Configuration:** Patients set up custom medication schedules (time, dosage), which are synced directly to the hardware.
4. **Monitoring & Analytics:** The system tracks real-time button-press behavior, analyzes adherence drift, and pushes actionable insights to the Physician Portal.

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

<img width="1120" height="956" alt="PillMate" src="https://github.com/user-attachments/assets/3ada4c9f-f643-4f5d-9797-a5bf3d49fecf" />

## Interface Gallery
<table>
  <tr>
    <td><img width="200" alt="profileScreens" src="https://github.com/user-attachments/assets/65a37707-31cb-4e5a-acf7-09ecc4aae2b2" /></td>
    <td><img width="200" alt="patientListScreen" src="https://github.com/user-attachments/assets/86481153-fd62-4a73-b3d8-3d45f6a04bab" /></td>
    <td><img width="200" alt="firstScreen" src="https://github.com/user-attachments/assets/3420d046-9cec-47f0-9238-d6266e77ddf9" /></td>
    <td><img width="200" alt="devicePairingScreen" src="https://github.com/user-attachments/assets/ce386d24-a0e2-4767-821c-39f2daa443b5" /></td>
  </tr>
  <tr>
    <td><img width="200" alt="configScreen" src="https://github.com/user-attachments/assets/f1218388-0cb9-4f12-9dc4-1ce8fe230fdf" /></td>
    <td><img width="200" alt="AuthScreen" src="https://github.com/user-attachments/assets/fccc81fa-60d7-4ead-ba32-5e880310d955" /></td>
    <td><img width="200" alt="AnalyticsScreen" src="https://github.com/user-attachments/assets/82ccebfc-85a3-4444-820f-5f2ccfac0745" /></td>
  </tr>
</table>

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
