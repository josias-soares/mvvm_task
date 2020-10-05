package com.example.tasks.service.helper

import android.content.Context
import android.os.Build
import androidx.biometric.BiometricManager

class FingerprintHelper {

    companion object {
        fun isAuthenticationAvailable(context: Context): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return false
            }

            val biometricManager: BiometricManager = BiometricManager.from(context)

            when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS -> return true
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> return false //Problem hardware
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> return false //Unavailable
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> return false //Not configured
            }

            return false
        }
    }
}