package com.anoki.randomfakenamegenerator.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.anoki.randomfakenamegenerator.utils.Event

interface PersonRepository {
    fun getPersons(
        context: Context,
        amount: Int = 1,
        gender: String = "random",
        region: String = "random"
    )
    val networkErrorEvent: LiveData<Event<String>>
}