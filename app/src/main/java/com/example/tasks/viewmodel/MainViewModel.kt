package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants.SHARED.PERSON_KEY
import com.example.tasks.service.constants.TaskConstants.SHARED.PERSON_NAME
import com.example.tasks.service.constants.TaskConstants.SHARED.TOKEN_KEY
import com.example.tasks.service.repository.local.SecurityPreferences

class MainViewModel(
    application: Application,
    private val mSharedPreferences: SecurityPreferences
) :
    AndroidViewModel(application) {

    private val mUserName = MutableLiveData<String>()
    var userName: LiveData<String> = mUserName

    private val mLogout = MutableLiveData<Boolean>()
    var logout: LiveData<Boolean> = mLogout

    fun loadUserName() {
        mUserName.value = mSharedPreferences.get(PERSON_NAME)
    }

    fun logout() {
        mSharedPreferences.remove(PERSON_NAME)
        mSharedPreferences.remove(PERSON_KEY)
        mSharedPreferences.remove(TOKEN_KEY)

        mLogout.value = true
    }
}