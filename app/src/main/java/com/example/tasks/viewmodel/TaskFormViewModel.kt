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

class TaskFormViewModel(
    application: Application,
    private val mPriorityRepository: PriorityRepository,
    private val mTaskRepository: TaskRepository
) : AndroidViewModel(application) {

    private val mPriorities = MutableLiveData<List<PriorityModel>>()
    var priorities: LiveData<List<PriorityModel>> = mPriorities

    private val mTaskCreated = MutableLiveData<ValidationListener>()
    var taskCreated: LiveData<ValidationListener> = mTaskCreated

    private val mTask = MutableLiveData<TaskModel>()
    var task: LiveData<TaskModel> = mTask

    fun listPriorities() {
        mPriorities.value = mPriorityRepository.list()
    }

    fun save(task: TaskModel) {
        if (task.id == 0) {
            mTaskRepository.create(task, object : APIListener<Boolean> {
                override fun onSuccess(model: Boolean) {
                    mTaskCreated.value = ValidationListener()
                }

                override fun onFailure(failure: String) {
                    mTaskCreated.value = ValidationListener(failure)
                }

            })
        } else {
            mTaskRepository.update(task, object : APIListener<Boolean> {
                override fun onSuccess(model: Boolean) {
                    mTaskCreated.value = ValidationListener()
                }

                override fun onFailure(failure: String) {
                    mTaskCreated.value = ValidationListener(failure)
                }

            })
        }
    }

    fun load(id: Int) {
        mTaskRepository.load(id, object : APIListener<TaskModel> {
            override fun onSuccess(model: TaskModel) {
                mTask.value = model
            }

            override fun onFailure(failure: String) {
                mTask.value = null
            }

        })
    }

}