package com.anoki.randomfakenamegenerator.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anoki.randomfakenamegenerator.model.Person
import com.anoki.randomfakenamegenerator.utils.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepositoryImpl : PersonRepository, KoinComponent, AnkoLogger {

    private val _service: WebService by inject()
    private val _db: PersonDatabase by inject()
    private val _networkErrorEvent = MutableLiveData<Event<String>>()

    override val networkErrorEvent: LiveData<Event<String>>
        get() = _networkErrorEvent

    override fun getPersons(
            context: Context,
            amount: Int,
            gender: String,
            region: String
    ) {
        if (amount > 1) {
            _service.getPersons(amount = amount, gender = gender, region = region)
                    .enqueue(object : Callback<List<Person>> {
                        /**
                         * Invoked when a network exception occurred talking to the server or when an unexpected
                         * exception occurred creating the request or processing the response.
                         */
                        override fun onFailure(call: Call<List<Person>>?, t: Throwable?) {
                            _networkErrorEvent.value =
                                    Event("Could not connect to the network. Please check your connection")
                            error { "Could not connect to the network. Please check your connection" }
                        }

                        /**
                         * Invoked for a received HTTP response.
                         *
                         *
                         * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
                         * Call [Response.isSuccessful] to determine if the response indicates success.
                         */
                        override fun onResponse(
                                call: Call<List<Person>>?,
                                response: Response<List<Person>>?
                        ) {
                            response?.body()?.let { responseBody ->
                                GlobalScope.launch {
                                    _db.personDao().save(responseBody)
                                }
                            }
                        }
                    })
        } else {
            _service.getPerson(gender = gender, region = region)
                    .enqueue(object : Callback<Person> {

                        /**
                         * Invoked when a network exception occurred talking to the server or when an unexpected
                         * exception occurred creating the request or processing the response.
                         */
                        override fun onFailure(call: Call<Person>?, t: Throwable?) {
                            _networkErrorEvent.value =
                                    Event("Could not connect to the network. Please check your connection")
                            error { "Could not connect to the network. Please check your connection" }
                        }

                        /**
                         * Invoked for a received HTTP response.
                         *
                         *
                         * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
                         * Call [Response.isSuccessful] to determine if the response indicates success.
                         */
                        override fun onResponse(
                                call: Call<Person>?,
                                response: Response<Person>?
                        ) {
                            response?.body()?.let { responseBody ->
                                GlobalScope.launch {
                                    _db.personDao().save(listOf(responseBody))
                                }
                            }
                        }
                    })
        }
    }
}