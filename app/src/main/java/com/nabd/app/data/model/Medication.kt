package com.nabd.app.data.model

data class Medication(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",
    val time: String = "08:00",
    val dosage: String = "1 Pill",
    val status: MedicationStatus = MedicationStatus.PENDING,
    val imageUrl: String? = null
)

enum class MedicationStatus {
    TAKEN, PENDING
}
