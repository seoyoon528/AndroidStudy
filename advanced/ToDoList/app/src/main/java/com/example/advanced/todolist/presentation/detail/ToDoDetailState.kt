package com.example.advanced.todolist.presentation.detail

import com.example.advanced.todolist.data.entity.ToDoEntity
import com.example.advanced.todolist.presentation.list.ToDoListState

sealed class ToDoDetailState {

    object UnInstalled: ToDoDetailState()

    object UnInitialized: ToDoDetailState()

    object Loading: ToDoDetailState()

    data class Success(
        val toDoItem: ToDoEntity
    ): ToDoDetailState()

    object Error: ToDoDetailState()
}
