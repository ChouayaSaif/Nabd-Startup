package com.nabd.app.data.model

data class Patient(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val isConnected: Boolean = false,
    val deviceCode: String? = null,
    val lastSync: String? = null
)
