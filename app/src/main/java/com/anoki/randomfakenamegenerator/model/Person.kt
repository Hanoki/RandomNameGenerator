package com.anoki.randomfakenamegenerator.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Person(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val surname: String,
    val gender: String,
    val region: String,
    val age: Int,
    val title: String,
    val phone: String,
    @Embedded
    val birthday: Birthday,
    val email: String,
    val password: String,
    @Embedded
    val credit_card: CreditCard,
    val photo: String
)