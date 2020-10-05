package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.service.constants.TaskConstants.HTTP.SUCCESS
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.repository.local.TaskDatabase
import com.example.tasks.service.repository.remote.PriorityService
import com.example.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(context: Context) : BaseRepository(context) {

    private val mRemote = RetrofitClient.createService(PriorityService::class.java)
    private val mPriorityDAO = TaskDatabase.getDatabase(context).priorityDAO()

    fun all() {
        if (!isConnectionAvailable()) {
            return
        }

        val call: Call<List<PriorityModel>> = mRemote.list()

        call.enqueue(object : Callback<List<PriorityModel>> {
            override fun onResponse(
                call: Call<List<PriorityModel>>, response: Response<List<PriorityModel>>
            ) {
                mPriorityDAO.clear()

                if (response.code() == SUCCESS) {
                    response.body()?.let {
                        save(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {}

        })
    }

    fun save(list: List<PriorityModel>) = mPriorityDAO.save(list)

    fun list() = mPriorityDAO.getAll()

    fun get(id: Int) = mPriorityDAO.getById(id)
}