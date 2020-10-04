package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants.HEADER.PERSON_KEY
import com.example.tasks.service.constants.TaskConstants.HEADER.TOKEN_KEY
import com.example.tasks.service.constants.TaskConstants.SHARED.PERSON_NAME
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.local.SecurityPreferences

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository = PersonRepository(application)
    private val mSharedPreferences = SecurityPreferences(application)

    private val mLogin = MutableLiveData<ValidationListener>()
    var login: LiveData<ValidationListener> = mLogin

    private val mLoggedUser = MutableLiveData<Boolean>()
    var loggedUser: LiveData<Boolean> = mLoggedUser

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        mPersonRepository.login(email, password, object : APIListener<HeaderModel> {
            override fun onSuccess(response: HeaderModel) {
                mSharedPreferences.store(TOKEN_KEY, response.token)
                mSharedPreferences.store(PERSON_KEY, response.personKey)
                mSharedPreferences.store(PERSON_NAME, response.name)

                mLogin.value = ValidationListener()
            }

            override fun onFailure(failure: String) {
                mLogin.value = ValidationListener(failure)
            }
        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
        val token = mSharedPreferences.get(TOKEN_KEY)
        val personKey = mSharedPreferences.get(PERSON_KEY)
        mLoggedUser.value = token.isNotEmpty() && personKey.isNotEmpty()

    }

}