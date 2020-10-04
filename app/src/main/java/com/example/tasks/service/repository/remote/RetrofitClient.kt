package com.example.tasks.service.repository.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {

        private const val baseUrl = "http://devmasterteam.com/CursoAndroidAPI/"

        private lateinit var retrofit: Retrofit
        private fun getRetrofitInstance(): Retrofit {
            val httClient = OkHttpClient.Builder()

            if (Companion::retrofit.isInitialized.not()) {
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit
        }

        fun <S> createService(serviceClass: Class<S>): S {
            return getRetrofitInstance().create(serviceClass)
        }
    }

}