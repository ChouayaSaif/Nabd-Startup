# Enable Patient Creation via Device Pairing

## Goal
Modify the "Confirm Connection" functionality in `DevicePairingScreen` to create a new patient in the database instead of only attempting to pair a device to an existing (but potentially null or incorrect) patient.

## User Review Required
- The current pairing logic assumes an existing `patientId` is passed down to `DevicePairingScreen`.
- To support creating a *new* patient, we will need to accept basic patient info (like a name) before or during the pairing flow.
- **Important**: Currently, the "Confirm Connection" button logic depends on a `patientId` parameter in the `pairDevice` function. I will add a new `createPatientAndPair` function in the `PatientViewModel`.

## Proposed Changes
### [MODIFY] [PatientViewModel.kt](file:///C:/Users/lenovo/Desktop/NABD/app/src/main/java/com/nabd/app/ui/viewmodel/PatientViewModel.kt)
- Add `createPatient(name: String, age: Int, gender: String, pairingCode: String, onResult: (String?) -> Unit)` function.
- This function will:
    1. Create a new patient document in Firestore with the pairing code.
    2. Set `isConnected = true`.
    3. Return the new `patientId` to the UI.

### [MODIFY] [DevicePairingScreen.kt](file:///C:/Users/lenovo/Desktop/NABD/app/src/main/java/com/nabd/app/ui/screens/DevicePairingScreen.kt)
- If `patientId` is null or empty, trigger the patient creation flow.
- Add a text field to capture the new patient's name if needed, or use a default name for now.

## Verification Plan
### Manual Verification
- Deploy the app.
- Go to the pairing screen.
- Click "Confirm Connection".
- Verify that a new patient card appears in the `PatientListScreen` with the linked status.
