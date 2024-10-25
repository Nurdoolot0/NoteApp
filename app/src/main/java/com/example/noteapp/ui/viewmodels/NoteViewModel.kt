package com.example.noteapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NoteViewModel : ViewModel() {
    private val _notificationTitle = MutableLiveData<String>()
    val notificationTitle: LiveData<String> get() = _notificationTitle

    private val _notificationMessage = MutableLiveData<String>()
    val notificationMessage: LiveData<String> get() = _notificationMessage

    // Метод для обновления уведомления
    fun updateNotification(title: String, message: String) {
        _notificationTitle.value = title
        _notificationMessage.value = message
    }
}
