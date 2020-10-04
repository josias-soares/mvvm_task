package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val mPriorityRepository = PriorityRepository(application)
    private val mTaskRepository = TaskRepository(application)

    private val mPriorities = MutableLiveData<List<PriorityModel>>()
    var priorities: LiveData<List<PriorityModel>> = mPriorities

    private val mTaskCreated = MutableLiveData<ValidationListener>()
    var taskCreated: LiveData<ValidationListener> = mTaskCreated

    fun listPriorities() {
        mPriorities.value = mPriorityRepository.list()
    }

    fun save(task: TaskModel) {
        mTaskRepository.create(task, object : APIListener<Boolean> {
            override fun onSuccess(response: Boolean) {
                mTaskCreated.value = ValidationListener()
            }

            override fun onFailure(failure: String) {
                mTaskCreated.value = ValidationListener(failure)
            }

        })
    }

}