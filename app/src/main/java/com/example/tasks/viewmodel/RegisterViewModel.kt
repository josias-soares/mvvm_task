package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.local.SecurityPreferences

class RegisterViewModel(
    application: Application,
    private val mPersonRepository: PersonRepository,
    private val mSharedPreferences: SecurityPreferences
) : AndroidViewModel(application) {

    private val mCreate = MutableLiveData<ValidationListener>()
    var create: LiveData<ValidationListener> = mCreate

    fun create(name: String, email: String, password: String) {
        mPersonRepository.create(name, email, password, object : APIListener<HeaderModel> {
            override fun onSuccess(model: HeaderModel) {
                mSharedPreferences.store(TaskConstants.HEADER.TOKEN_KEY, model.token)
                mSharedPreferences.store(TaskConstants.HEADER.PERSON_KEY, model.personKey)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_NAME, model.name)

                mCreate.value = ValidationListener()
            }

            override fun onFailure(failure: String) {
                mCreate.value = ValidationListener(failure)
            }

        })
    }

}