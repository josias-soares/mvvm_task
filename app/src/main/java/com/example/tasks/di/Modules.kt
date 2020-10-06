package com.example.tasks.di

import androidx.room.Room
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.repository.*
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.service.repository.local.TaskDatabase
import com.example.tasks.service.repository.remote.PersonService
import com.example.tasks.service.repository.remote.PriorityService
import com.example.tasks.service.repository.remote.TaskService
import com.example.tasks.viewmodel.*
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { SecurityPreferences(androidApplication()) }
}

val daoModule = module {
    single {
        Room.databaseBuilder(get(), TaskDatabase::class.java, "tasksDB")
            .allowMainThreadQueries()
            .build()
    }

    single { get<TaskDatabase>().priorityDAO() }
}

val repositoryModule = module {
    single<TaskRepository> { TaskRepositoryImpl(androidContext(), get()) }
    single<PriorityRepository> { PriorityRepositoryImpl(androidContext(), get(), get()) }
    single<PersonRepository> { PersonRepositoryImpl(androidContext(), get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(androidApplication(), get()) }
    viewModel { RegisterViewModel(androidApplication(), get(), get()) }
    viewModel { TaskFormViewModel(androidApplication(), get(), get()) }
    viewModel { AllTasksViewModel(androidApplication(), get()) }
    viewModel { LoginViewModel(androidApplication(), get(), get(), get()) }
}

val serviceModule = module {

    single { retrofit(TaskConstants.RETROFIT.BASE_URL, get()) }

    single { get<Retrofit>().create(TaskService::class.java) }
    single { get<Retrofit>().create(PersonService::class.java) }
    single { get<Retrofit>().create(PriorityService::class.java) }
}

private fun retrofit(baseUrl: String, prefs: SecurityPreferences) = Retrofit.Builder()
    .callFactory(OkHttpClient.Builder().addInterceptor { chain ->
        val tokenKey = prefs.get(TaskConstants.HEADER.TOKEN_KEY)
        val personKey = prefs.get(TaskConstants.HEADER.PERSON_KEY)

        val request = chain.request()
            .newBuilder()
            .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
            .addHeader(TaskConstants.HEADER.TOKEN_KEY, tokenKey)
            .build()

        chain.proceed(request)

    }.build())
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .build()