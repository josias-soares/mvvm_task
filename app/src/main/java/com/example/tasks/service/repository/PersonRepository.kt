package com.example.tasks.service.repository

import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.model.HeaderModel

interface PersonRepository {
    fun login(email: String, password: String, listener: APIListener<HeaderModel>)

    fun create(name: String, email: String, password: String, listener: APIListener<HeaderModel>)
}