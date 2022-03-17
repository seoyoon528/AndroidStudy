package com.example.advanced.todolist

import android.app.Application
import com.example.advanced.todolist.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ToDoListApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // TODO Koin Trigger
        startKoin {
            androidLogger(Level.ERROR)      // 에러 발생시 로깅
            androidContext(this@ToDoListApplication)
            modules(AppModule)       // di에서 모듈 생성하여 넣어줌
        }
    }
}