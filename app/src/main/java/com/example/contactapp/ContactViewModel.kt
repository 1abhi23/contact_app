package com.example.contactapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.contactapp.model.ContactRepository
import com.example.contactapp.model.Contacts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactViewModel(private val repository: ContactRepository): ViewModel() {
    private val _contacts = MutableStateFlow<List<Contacts>>(emptyList())
    val contacts: StateFlow<List<Contacts>> = _contacts.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allContacts.collect { list ->
                _contacts.value = list
            }
        }
    }

    fun addContact(image: String, name: String, phoneNumber: String, email: String) {
        viewModelScope.launch {
            val contact = Contacts(0, image = image, email = email, name = name, phoneNumber = phoneNumber)
            repository.insert(contact)
        }
    }

    fun updateContact(contact: Contacts) {
        viewModelScope.launch {
           repository.update(contact)
        }
    }

    fun deleteContact(contact: Contacts) {
        viewModelScope.launch {
            repository.delete(contact)
        }
    }

}

class ContactViewModelFactory(private val repository: ContactRepository): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            return ContactViewModel(repository) as T
        else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}