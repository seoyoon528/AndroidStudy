package com.example.advanced.shoppingmall

import android.app.Application
import com.example.advanced.shoppingmall.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ShoppingMallApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        //  Application OnCreate 시점에 AppModule 주입
        startKoin {         // DI에 대한 로딩
            androidLogger(Level.ERROR)
            androidContext(this@ShoppingMallApplication)
            modules(AppModule)
        }
    }
}