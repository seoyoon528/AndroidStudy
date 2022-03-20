package com.example.advanced.todolist.di

import com.example.advanced.todolist.data.repository.TestToDoRepository
import com.example.advanced.todolist.data.repository.ToDoRepository
import com.example.advanced.todolist.domain.todo.*
import com.example.advanced.todolist.presentation.detail.DetailMode
import com.example.advanced.todolist.presentation.detail.DetailViewModel
import com.example.advanced.todolist.presentation.list.ListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val AppTestModule = module {

    // ViewModel
    viewModel { ListViewModel(get(), get(), get()) }      // ListViewModel 주입받음
    viewModel{ (detailMode: DetailMode, id: Long) ->        //  인자 값을 받아서
        DetailViewModel(
            detailMode = detailMode,
            id = id,
            get(),       // getToDoItemUseCase
            get(),      // deleteToDoItemUseCase
            get(),       // updateToDoUseCase
            get()       //  insertToDoItemUseCase
        )
    }       // detailViewModel 주입받음

    // UseCase
    factory { GetToDoListUseCase(get()) }        // factory 패턴
    factory { InsertToDoListUseCase(get()) }        // get()으로 해당 레포지토리 얻어옴
    factory { InsertToDoItemUseCase(get()) }
    factory { UpdateToDoListUseCase(get()) }
    factory { GetToDoItemUseCase(get()) }
    factory { DeleteAllToDoItemUseCase(get()) }
    factory { DeleteToDoItemUseCase(get()) }

    // Repository
    single<ToDoRepository> { TestToDoRepository() }
}