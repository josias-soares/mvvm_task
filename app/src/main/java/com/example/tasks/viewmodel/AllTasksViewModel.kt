package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {
    private val mTaskRepository = TaskRepository(application)

    private val mTasks = MutableLiveData<List<TaskModel>>()
    var tasks: LiveData<List<TaskModel>> = mTasks

    fun list() {
        mTaskRepository.all(object : APIListener<List<TaskModel>> {
            override fun onSuccess(response: List<TaskModel>) {
                mTasks.value = response
            }

            override fun onFailure(failure: String) {
                mTasks.value = arrayListOf()
            }

        })
    }


}