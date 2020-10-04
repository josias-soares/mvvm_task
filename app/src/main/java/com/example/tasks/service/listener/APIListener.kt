package com.example.tasks.service.listener

interface APIListener<T> {
    fun onSuccess(response: T)
    fun onFailure(failure: String)
}