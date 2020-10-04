package com.example.tasks.service.model

import com.google.gson.annotations.SerializedName

class PriorityModel {
    @SerializedName("Id")
    var id: Int = 0

    @SerializedName("Description")
    var description: String = ""
}