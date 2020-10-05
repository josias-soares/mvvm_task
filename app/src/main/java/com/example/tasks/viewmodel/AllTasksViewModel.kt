package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {
    private val mTaskRepository = TaskRepository(application)

    private val mTasks = MutableLiveData<List<TaskModel>>()
    var tasks: LiveData<List<TaskModel>> = mTasks

    private val mTaskRemoved = MutableLiveData<ValidationListener>()
    var taskRemoved: LiveData<ValidationListener> = mTaskRemoved

    private val mTaskUpdate = MutableLiveData<ValidationListener>()
    var taskUpdate: LiveData<ValidationListener> = mTaskUpdate

    fun list() {
        mTaskRepository.all(object : APIListener<List<TaskModel>> {
            override fun onSuccess(model: List<TaskModel>) {
                mTasks.value = model
            }

            override fun onFailure(failure: String) {
                mTasks.value = arrayListOf()
            }

        })
    }

    fun complete(id: Int) {
        updateStatus(id, true)
    }

    fun undo(id: Int) {
        updateStatus(id, false)
    }

    private fun updateStatus(id: Int, complete: Boolean) {
        mTaskRepository.updateStatus(id, complete, object : APIListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                list()
                mTaskUpdate.value = ValidationListener()
            }

            override fun onFailure(failure: String) {
                mTaskUpdate.value = ValidationListener(failure)
            }
        })
    }


    fun delete(id: Int) {
        mTaskRepository.delete(id, object : APIListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                list()
                mTaskRemoved.value = ValidationListener()
            }

            override fun onFailure(failure: String) {
                mTaskRemoved.value = ValidationListener(failure)
            }
        })
    }
}