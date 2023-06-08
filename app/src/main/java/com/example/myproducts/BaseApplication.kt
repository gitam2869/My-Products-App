package com.example.myproducts

import android.app.Application
import com.example.myproducts.di.apiModule
import com.example.myproducts.di.netModule
import com.example.myproducts.di.repositoryModule
import com.example.myproducts.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BaseApplication)
            modules(
                listOf(
                    apiModule,
                    netModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }
}