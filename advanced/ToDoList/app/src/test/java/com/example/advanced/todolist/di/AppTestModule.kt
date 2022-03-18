package com.example.advanced.todolist.di

import com.example.advanced.todolist.data.repository.TestToDoRepository
import com.example.advanced.todolist.data.repository.ToDoRepository
import com.example.advanced.todolist.domain.todo.*
import com.example.advanced.todolist.presentation.list.ListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val AppTestModule = module {

    // ViewModel
    viewModel { ListViewModel(get(), get(), get()) }      // ListViewModel 주입받음

    // UseCase
    factory { GetToDoListUseCase(get()) }        // factory 패턴
    factory { InsertToDoListUseCase(get()) }        // get()으로 해당 레포지토리 얻어옴
    factory { UpdateToDoListUseCase(get()) }
    factory { GetToDoItemUseCase(get()) }
    factory { DeleteAllToDoItemUseCase(get()) }

    // Repository
    single<ToDoRepository> { TestToDoRepository() }
}