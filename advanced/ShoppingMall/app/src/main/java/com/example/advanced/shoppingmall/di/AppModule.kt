package com.example.advanced.shoppingmall.di

import com.example.advanced.shoppingmall.data.network.buildOkHttpClient
import com.example.advanced.shoppingmall.data.network.provideGsonConverterFactory
import com.example.advanced.shoppingmall.data.network.provideProductApiService
import com.example.advanced.shoppingmall.data.network.provideProductRetrofit
import com.example.advanced.shoppingmall.data.repository.DefaultProductRepository
import com.example.advanced.shoppingmall.data.repository.ProductRepository
import com.example.advanced.shoppingmall.domain.GetProductItemUseCase
import com.example.advanced.shoppingmall.domain.GetProductListUseCase
import com.example.advanced.shoppingmall.presentation.list.ProductListViewModel
import com.example.advanced.shoppingmall.presentation.main.MainViewModel
import com.example.advanced.shoppingmall.presentation.profile.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.experimental.dsl.viewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    // Koin에 주입

    // ViewModels
    viewModel { MainViewModel() }
    viewModel { ProductListViewModel(get()) }
    viewModel { ProfileViewModel() }

    // Coroutines Dispatchers
    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // UseCases
    factory { GetProductItemUseCase(get()) }
    factory { GetProductListUseCase(get()) }

    // Repositories
    single<ProductRepository> { DefaultProductRepository(get(), get(), get()) }         // ProductRepository를 interface type으로 주입받을 수 있게됨

    single { provideGsonConverterFactory() }

    single { buildOkHttpClient() }

    single { provideProductRetrofit(get(), get()) }

    single { provideProductApiService(get()) }
}