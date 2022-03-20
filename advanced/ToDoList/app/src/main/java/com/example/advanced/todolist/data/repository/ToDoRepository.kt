package com.example.advanced.todolist.data.repository

import com.example.advanced.todolist.data.entity.ToDoEntity

// Repository 패턴을 통해 어떠한 함수를 추상화하여 호출하는 경우, 각 주입된 인스턴스에 맞게 원하는 데이터를 불러올 수 있음
interface ToDoRepository {

    // Coroutine
    suspend fun getToDoList(): List<ToDoEntity>

    suspend fun insertToDoItem(toDoItem: ToDoEntity): Long

    suspend fun insertToDoList(toDoList: List<ToDoEntity>)

    suspend fun updateToDoItem(toDoItem: ToDoEntity) : Boolean

    suspend fun getToDoItem(itemId: Long): ToDoEntity?

    suspend fun deleteAll()

    suspend fun deleteToDoItem(itemId: Long): Boolean       //  지워졌는지 여부 확인을 위해 Boolean

}