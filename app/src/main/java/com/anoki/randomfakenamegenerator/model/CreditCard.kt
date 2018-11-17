package com.anoki.randomfakenamegenerator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CreditCard(
    @PrimaryKey(autoGenerate = true)
    val creditCard_id: Int,
    val expiration: String,
    val number: String,
    val pin: Int,
    val security: Int
)