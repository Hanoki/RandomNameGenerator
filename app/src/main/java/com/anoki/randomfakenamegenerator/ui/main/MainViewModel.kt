package com.anoki.randomfakenamegenerator.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.anoki.randomfakenamegenerator.model.Person
import com.anoki.randomfakenamegenerator.repository.PersonDatabase
import com.anoki.randomfakenamegenerator.repository.PersonRepository
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class MainViewModel(private val context: Context, private val personRepository: PersonRepository) :
    ViewModel(), KoinComponent {

    val persons: LiveData<PagedList<Person>>
    val networkErrorEvent = personRepository.networkErrorEvent

    private val personDatabase: PersonDatabase by inject()

    init {

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(10)
            .build()

        persons = LivePagedListBuilder(personDatabase.personDao().load(), pagedListConfig).build()
    }

    fun generate(amount: Int, gender: String, region: String) {
        personRepository.getPersons(
            context,
            amount = amount,
            gender = gender.toLowerCase(),
            region = region.toLowerCase()
        )
    }
}
