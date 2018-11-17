package com.anoki.randomfakenamegenerator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Birthday(
    @PrimaryKey(autoGenerate = true)
    val birthday_id: Int,
    val dmy: String,
    val mdy: String,
    val raw: Int
)