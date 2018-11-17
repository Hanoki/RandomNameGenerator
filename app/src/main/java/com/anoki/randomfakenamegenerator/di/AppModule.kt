package com.android.anoki.randomfakenamegenerator.di

import androidx.room.Room
import com.anoki.randomfakenamegenerator.repository.PersonDatabase
import com.anoki.randomfakenamegenerator.repository.PersonRepository
import com.anoki.randomfakenamegenerator.repository.PersonRepositoryImpl
import com.anoki.randomfakenamegenerator.repository.WebService
import com.anoki.randomfakenamegenerator.ui.main.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    single { PersonRepositoryImpl() as PersonRepository }

    viewModel { MainViewModel(androidContext(), get()) }

    single {
        Room.databaseBuilder(
                androidContext(),
                PersonDatabase::class.java, "person_database"
        ).build()
    }

    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS).addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://uinames.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        retrofit.create<WebService>(WebService::class.java)
    }
}