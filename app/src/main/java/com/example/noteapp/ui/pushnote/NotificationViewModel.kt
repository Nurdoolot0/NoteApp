package com.example.noteapp.ui.pushnote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationViewModel : ViewModel() {
    val notificationData: MutableLiveData<Pair<String?, String?>> = MutableLiveData()

    fun setNotificationData(title: String?, message: String?) {
        notificationData.postValue(Pair(title, message))
    }
}
