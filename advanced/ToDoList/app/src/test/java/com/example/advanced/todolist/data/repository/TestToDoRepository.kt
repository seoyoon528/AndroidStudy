package com.example.advanced.todolist.data.repository

import com.example.advanced.todolist.data.entity.ToDoEntity

class TestToDoRepository: ToDoRepository {

    // 메모리 캐싱용으로 빈 리스트 생성
    private val  toDoList: MutableList<ToDoEntity> = mutableListOf()

    override suspend fun getToDoList(): List<ToDoEntity> {
        return toDoList
    }

    override suspend fun insertToDoItem(toDoItem: ToDoEntity) {
        this.toDoList.add(toDoItem)
    }

    override suspend fun insertToDoList(toDoList: List<ToDoEntity>) {
        this.toDoList.addAll(toDoList)
    }

    override suspend fun updateToDoItem(toDoItem: ToDoEntity): Boolean {
        val foundToDoEntity = toDoList.find { it.id == toDoItem.id }        //  ToDoList에 같은 아이디인 아이템을 찾아서

        return if (foundToDoEntity == null) {
            false
        } else {
            this.toDoList[toDoList.indexOf(foundToDoEntity)] = toDoItem     //  교체
            true
        }
    }

    override suspend fun getToDoItem(itemId: Long): ToDoEntity? {
        return toDoList.find { it.id == itemId }
    }

    override suspend fun deleteAll() {
        toDoList.clear()
    }
}