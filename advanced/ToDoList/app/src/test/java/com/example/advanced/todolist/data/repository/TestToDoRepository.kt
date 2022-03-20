package com.example.advanced.todolist.data.repository

import com.example.advanced.todolist.data.entity.ToDoEntity

class TestToDoRepository: ToDoRepository {

    // 메모리 캐싱용으로 빈 리스트 생성
    private val  toDoList: MutableList<ToDoEntity> = mutableListOf()

    override suspend fun getToDoList(): List<ToDoEntity> {
        return toDoList
    }

    override suspend fun insertToDoItem(toDoItem: ToDoEntity): Long{
        this.toDoList.add(toDoItem)
        return toDoItem.id
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

    override suspend fun deleteToDoItem(itemId: Long): Boolean {
        val foundToDoEntity = toDoList.find { it.id == itemId }

        return if (foundToDoEntity == null) {
            false
        } else {
            this.toDoList.removeAt(toDoList.indexOf(foundToDoEntity))       // 아이디에 해당하는 아이템 삭제
            true
        }
    }


}