package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants.HEADER.PERSON_KEY
import com.example.tasks.service.constants.TaskConstants.HEADER.TOKEN_KEY
import com.example.tasks.service.constants.TaskConstants.SHARED.PERSON_NAME
import com.example.tasks.service.helper.FingerprintHelper
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository = PersonRepository(application)
    private val mPriorityRepository = PriorityRepository(application)
    private val mSharedPreferences = SecurityPreferences(application)

    private val mLogin = MutableLiveData<ValidationListener>()
    var login: LiveData<ValidationListener> = mLogin

    private val mFingerprint = MutableLiveData<Boolean>()
    var fingerprint: LiveData<Boolean> = mFingerprint

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        mPersonRepository.login(email, password, object : APIListener<HeaderModel> {
            override fun onSuccess(model: HeaderModel) {
                mSharedPreferences.store(TOKEN_KEY, model.token)
                mSharedPreferences.store(PERSON_KEY, model.personKey)
                mSharedPreferences.store(PERSON_NAME, model.name)

                RetrofitClient.addHeader(model.token, model.personKey)

                mLogin.value = ValidationListener()
            }

            override fun onFailure(failure: String) {
                mLogin.value = ValidationListener(failure)
            }
        })
    }

    fun isAuthenticationAvailable() {
        val tokenKey = mSharedPreferences.get(TOKEN_KEY)
        val personKey = mSharedPreferences.get(PERSON_KEY)

        val everLogged = tokenKey.isNotEmpty() && personKey.isNotEmpty()
        if (!everLogged) {
            mPriorityRepository.all()
        }

        RetrofitClient.addHeader(tokenKey, personKey)

        if (FingerprintHelper.isAuthenticationAvailable(getApplication())) {
            mFingerprint.value = everLogged
        }
    }

}