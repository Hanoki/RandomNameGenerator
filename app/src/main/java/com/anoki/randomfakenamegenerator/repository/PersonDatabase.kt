package com.anoki.randomfakenamegenerator.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anoki.randomfakenamegenerator.model.Birthday
import com.anoki.randomfakenamegenerator.model.CreditCard
import com.anoki.randomfakenamegenerator.model.Person

@Database(entities = [Person::class, Birthday::class, CreditCard::class], version = 2)
abstract class PersonDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
}