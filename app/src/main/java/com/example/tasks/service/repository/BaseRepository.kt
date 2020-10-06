package com.example.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.tasks.service.constants.TaskConstants
import com.google.gson.Gson


open class BaseRepository(context: Context) {

    private val mContext: Context = context

    fun fail(code: Int) = code != TaskConstants.HTTP.SUCCESS

    fun failResponse(response: String): String {
        return Gson().fromJson(response, String::class.java)
    }

    /**
     * Verifica se existe conexão com internet
     */
    fun isConnectionAvailable(): Boolean {
        var result = false
        val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = cm.activeNetwork ?: return false
            val actNw = cm.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            cm.run {
                cm.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

}