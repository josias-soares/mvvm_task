package com.example.tasks.service.repository

import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.model.TaskModel

interface TaskRepository {
    fun all(listener: APIListener<List<TaskModel>>)

    fun nextWeek(listener: APIListener<List<TaskModel>>)

    fun overdue(listener: APIListener<List<TaskModel>>)

    fun create(task: TaskModel, listener: APIListener<Boolean>)

    fun load(id: Int, listener: APIListener<TaskModel>)

    fun update(task: TaskModel, listener: APIListener<Boolean>)

    fun complete(id: Int, complete: Boolean, listener: APIListener<Boolean>)

    fun delete(id: Int, listener: APIListener<Boolean>)
}