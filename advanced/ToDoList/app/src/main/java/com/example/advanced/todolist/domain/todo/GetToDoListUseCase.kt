package com.example.advanced.todolist.domain.todo

import com.example.advanced.todolist.data.entity.ToDoEntity
import com.example.advanced.todolist.data.repository.ToDoRepository
import com.example.advanced.todolist.domain.UseCase

internal class GetToDoListUseCase (
    private val toDoRepository: ToDoRepository
): UseCase {

    suspend operator fun invoke(): List<ToDoEntity> {
        return toDoRepository.getToDoList()
    }
}