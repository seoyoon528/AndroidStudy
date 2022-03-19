package com.example.advanced.todolist.domain.todo

import com.example.advanced.todolist.data.entity.ToDoEntity
import com.example.advanced.todolist.data.repository.ToDoRepository
import com.example.advanced.todolist.domain.UseCase

internal class InsertToDoItemUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

        suspend operator fun invoke (toDoItem: ToDoEntity) {
            return toDoRepository.insertToDoItem(toDoItem)
        }
}