package com.example.tasks.service.repository.remote

import com.example.tasks.BuildConfig
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.constants.TaskConstants.RETROFIT.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HeaderHelper {

    companion object {

        var personKey: String = ""
        var tokenKey: String = ""

        fun addHeader(tokenKey: String, personKey: String) {
            this.personKey = personKey
            this.tokenKey = tokenKey
        }
    }
}