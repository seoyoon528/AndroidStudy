package com.example.advanced.shoppingmall.di

import com.example.advanced.shoppingmall.data.network.buildOkHttpClient
import com.example.advanced.shoppingmall.data.network.provideGsonConverterFactory
import com.example.advanced.shoppingmall.data.network.provideProductApiService
import com.example.advanced.shoppingmall.data.network.provideProductRetrofit
import com.example.advanced.shoppingmall.data.repository.DefaultProductRepository
import com.example.advanced.shoppingmall.data.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val AppModule = module {
    // Koin에 주입

    // Coroutines Dispatchers
    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // Repositories
    single<ProductRepository> { DefaultProductRepository(get(), get(), get()) }         // ProductRepository를 interface type으로 주입받을 수 있게됨

    single { provideGsonConverterFactory() }

    single { buildOkHttpClient() }

    single { provideProductRetrofit(get(), get()) }

    single { provideProductApiService(get()) }
}