package com.example.tasks.service.repository.remote

import com.example.tasks.BuildConfig
import com.example.tasks.service.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {

        private lateinit var retrofit: Retrofit
        private const val baseUrl = "http://devmasterteam.com/CursoAndroidAPI/"

        private var personKey: String = ""
        private var tokenKey: String = ""

        private fun getRetrofitInstance(): Retrofit {
            val httClient = OkHttpClient.Builder()

            httClient.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    if (BuildConfig.DEBUG) {
                        chain.request().body()?.let {
                            println("==> REQUEST: ${it.toString()}")
                        }
                    }

                    val request = chain.request()
                        .newBuilder()
                        .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                        .addHeader(TaskConstants.HEADER.TOKEN_KEY, tokenKey)
                        .build()

                    return chain.proceed(request)

                }
            })

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

        fun addHeader(tokenKey: String, personKey: String) {
            this.personKey = personKey
            this.tokenKey = tokenKey
        }
    }

}