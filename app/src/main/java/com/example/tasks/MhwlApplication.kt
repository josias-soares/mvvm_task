package com.example.tasks

import android.app.Application
import com.example.tasks.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class MhwlApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MhwlApplication)
            modules(
                listOf(
                    appModule,
                    repositoryModule,
                    viewModelModule,
                    serviceModule,
                    daoModule
                )
            )
        }
    }
}