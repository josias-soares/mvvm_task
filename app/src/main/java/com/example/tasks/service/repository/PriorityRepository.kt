package com.example.tasks.service.repository

import com.example.tasks.service.model.PriorityModel

interface PriorityRepository {
    fun all()

    fun save(list: List<PriorityModel>)

    fun list() : List<PriorityModel>

    fun get(id: Int) : PriorityModel
}