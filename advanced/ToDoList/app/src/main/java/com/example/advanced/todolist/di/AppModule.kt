package com.example.advanced.todolist.di

import android.content.Context
import androidx.room.Room
import com.example.advanced.todolist.data.local.db.ToDoDatabase
import com.example.advanced.todolist.data.repository.DefaultToDoRepository
import com.example.advanced.todolist.data.repository.ToDoRepository
import com.example.advanced.todolist.domain.todo.*
import com.example.advanced.todolist.domain.todo.DeleteAllToDoItemUseCase
import com.example.advanced.todolist.domain.todo.GetToDoListUseCase
import com.example.advanced.todolist.domain.todo.InsertToDoItemUseCase
import com.example.advanced.todolist.domain.todo.InsertToDoListUseCase
import com.example.advanced.todolist.domain.todo.UpdateToDoListUseCase
import com.example.advanced.todolist.presentation.detail.DetailMode
import com.example.advanced.todolist.presentation.detail.DetailViewModel
import com.example.advanced.todolist.presentation.list.ListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val AppModule = module {

    single { Dispatchers.Main }
    single { Dispatchers.IO }

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
    single<ToDoRepository> { DefaultToDoRepository(get(), get()) }

    single { provideDB(androidApplication()) }
    single { provideToDoDao(get()) }
}

// DefaultToDoRepository()에서 쓸 수 있도록 주입시킬 Dao와 DB
internal fun provideDB(context: Context): ToDoDatabase =
    Room.databaseBuilder(context, ToDoDatabase::class.java, ToDoDatabase.DB_NAME).build()

internal fun provideToDoDao(database: ToDoDatabase) = database.toDoDao()