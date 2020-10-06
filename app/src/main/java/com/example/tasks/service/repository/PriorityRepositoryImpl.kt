package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.service.constants.TaskConstants.HTTP.SUCCESS
import com.example.tasks.service.helper.ConnectionHelper.Companion.isConnectionAvailable
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.repository.local.PriorityDAO
import com.example.tasks.service.repository.remote.PriorityService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepositoryImpl(
    val context: Context,
    val service: PriorityService,
    val priorityDAO: PriorityDAO
) : PriorityRepository {

    override fun all() {
        if (!isConnectionAvailable(context)) {
            return
        }

        val call: Call<List<PriorityModel>> = service.list()

        call.enqueue(object : Callback<List<PriorityModel>> {
            override fun onResponse(
                call: Call<List<PriorityModel>>, response: Response<List<PriorityModel>>
            ) {
                priorityDAO.clear()

                if (response.code() == SUCCESS) {
                    response.body()?.let {
                        println("========> PRIORITY: ${it.toString()}")
                        save(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {}

        })
    }

    override fun save(list: List<PriorityModel>) = priorityDAO.save(list)

    override fun list() = priorityDAO.getAll()

    override fun get(id: Int) = priorityDAO.getById(id)
}