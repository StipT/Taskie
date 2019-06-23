package com.tstipanic.taskie.di

import com.tstipanic.taskie.common.AUTH_INTERCEPTOR
import com.tstipanic.taskie.common.BASE_URL
import com.tstipanic.taskie.common.KEY_AUTHORIZATION
import com.tstipanic.taskie.common.LOGGING_INTERCEPTOR
import com.tstipanic.taskie.model.interactor.Interactor
import com.tstipanic.taskie.model.interactor.InteractorImpl
import com.tstipanic.taskie.networking.ApiService
import com.tstipanic.taskie.prefs.provideSharedPrefs
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkingModule = module {

    factory {
        provideSharedPrefs().getUserToken()
    }

    //INTERCEPTORS
    single(named(LOGGING_INTERCEPTOR)) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    single(named(AUTH_INTERCEPTOR)) {
        Interceptor {
            val request = it.request().newBuilder()
                .addHeader(KEY_AUTHORIZATION, get())
                .build()
            it.proceed(request)
        }
    }

    //OKHTTPCLIENT
    single {
        OkHttpClient.Builder()
            .addInterceptor(get(named(LOGGING_INTERCEPTOR)))
            .addInterceptor(get(named(AUTH_INTERCEPTOR)))
            .build()
    }

    //RETROFIT
    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(ApiService::class.java) }

    factory<Interactor> { InteractorImpl(get()) }
}