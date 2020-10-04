package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.local.SecurityPreferences

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository = PersonRepository(application)
    private val mSharedPreferences = SecurityPreferences(application)

    private val mCreate = MutableLiveData<ValidationListener>()
    var create: LiveData<ValidationListener> = mCreate

    fun create(name: String, email: String, password: String) {
        mPersonRepository.create(name, email, password, object : APIListener<HeaderModel> {
            override fun onSuccess(response: HeaderModel) {
                mSharedPreferences.store(TaskConstants.HEADER.TOKEN_KEY, response.token)
                mSharedPreferences.store(TaskConstants.HEADER.PERSON_KEY, response.personKey)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_NAME, response.name)

                mCreate.value = ValidationListener()
            }

            override fun onFailure(failure: String) {
                mCreate.value = ValidationListener(failure)
            }

        })
    }

}