package com.anoki.randomfakenamegenerator.repository

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anoki.randomfakenamegenerator.model.Person

@Dao
interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(persons: List<Person>)

    @Query("SELECT * FROM person ORDER BY id DESC")
    fun load(): DataSource.Factory<Int, Person>
}