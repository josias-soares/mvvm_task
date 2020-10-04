package com.example.tasks.service.listener

class ValidationListener(message: String = "") {
    private var mSuccess: Boolean = true
        get() = field
    private var mMessage: String = ""
        get() = field

    init {
        if (message.isNotEmpty()){
            mSuccess = false
            mMessage = message
        }
    }

    fun success() = mSuccess
    fun failure() = mMessage
}