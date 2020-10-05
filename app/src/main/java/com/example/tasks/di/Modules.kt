package com.example.tasks.di

import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.PriorityRepositoryImpl
import com.example.tasks.service.repository.TaskRepository
import com.example.tasks.service.repository.TaskRepositoryImpl
import com.example.tasks.viewmodel.TaskFormViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<PriorityRepository> { PriorityRepositoryImpl(androidContext()) }
    single<TaskRepository> { TaskRepositoryImpl(androidContext()) }

}

val viewModelModule = module {
    viewModel { TaskFormViewModel(androidApplication(), get(), get()) }
}