package com.example.advanced.todolist.domain.todo

import com.example.advanced.todolist.data.entity.ToDoEntity
import com.example.advanced.todolist.data.repository.ToDoRepository
import com.example.advanced.todolist.domain.UseCase

class GetToDoItemUseCase (
    private val toDoRepository: ToDoRepository
): UseCase {

    suspend operator fun invoke(itemId: Long): ToDoEntity? {
        return toDoRepository.getToDoItem(itemId)
    }
}