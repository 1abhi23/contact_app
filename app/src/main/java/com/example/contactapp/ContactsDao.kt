package com.example.contactapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contacts)

    @Update
    suspend fun update(contact: Contacts)

    @Delete
    suspend fun delete(contact: Contacts)

    @Query("SELECT * FROM contacts")
    fun getAllContacts(): Flow<List<Contacts>>
}