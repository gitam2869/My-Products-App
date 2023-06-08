package com.example.myproducts.di

import com.example.myproducts.data.api.ApiServices
import com.example.myproducts.data.repository.ProductRepository
import com.example.myproducts.utils.Constants
import com.example.myproducts.viewmodel.AddProductViewModel
import com.example.myproducts.viewmodel.ProductListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val apiModule = module {
    fun provideUserApi(retrofit: Retrofit): ApiServices {
        return retrofit.create(ApiServices::class.java)
    }

    single { provideUserApi(get()) }
}

val netModule = module {
    fun provideHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)

        return okHttpClient.build()
    }

    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    single { provideHttpClient() }
    single { provideRetrofit(get()) }
}

val repositoryModule = module {
    single { ProductRepository(get()) }
}

val viewModelModule = module {
    single { ProductListViewModel(get()) }
    single { AddProductViewModel(get()) }
}