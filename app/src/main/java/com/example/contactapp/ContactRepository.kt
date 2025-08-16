package com.example.contactapp

import kotlinx.coroutines.flow.Flow

class ContactRepository(private val contactDao: ContactsDao) {

    val allContacts: Flow<List<Contacts>> = contactDao.getAllContacts()

    suspend fun insert(contact:Contacts) {
        contactDao.insert(contact)
    }

    suspend fun update(contact:Contacts) {
        contactDao.update(contact)
    }

    suspend fun delete(contact:Contacts) {
        contactDao.delete(contact)
    }
}