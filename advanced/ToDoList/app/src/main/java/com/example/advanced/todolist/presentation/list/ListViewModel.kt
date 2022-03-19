package com.example.advanced.todolist.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advanced.todolist.data.entity.ToDoEntity
import com.example.advanced.todolist.domain.todo.DeleteAllToDoItemUseCase
import com.example.advanced.todolist.domain.todo.GetToDoListUseCase
import com.example.advanced.todolist.domain.todo.UpdateToDoListUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * 필요한 UseCase
 * 1. [GetToDoListUseCase]
 * 2. [UpdateToDoUseCase]
 * 3. [DeleteAllToDoItemUseCase]
 */

internal class ListViewModel(

    private val getToDoListUseCase: GetToDoListUseCase,
    private val updateToDoListUseCase: UpdateToDoListUseCase,
    private val deleteAllToDoItemUseCase: DeleteAllToDoItemUseCase

): BaseViewModel() {

    private var _toDoListLiveData = MutableLiveData<ToDoListState>(ToDoListState.UnInitialized)     // 처음 state = Uninitialized
    val todoListLiveData: LiveData<ToDoListState> = _toDoListLiveData

    // Coroutine Block 생성
    override fun fetchData(): Job = viewModelScope.launch {
        _toDoListLiveData.postValue(ToDoListState.Loading)      //  데이터 Loading
        _toDoListLiveData.postValue(ToDoListState.Success(getToDoListUseCase()))       // 데이터를 성공적으로 불러왔을 경우, 데이터가 반영되도록 postValue에 넣어줌
    }

    fun updateEntity(todoEntity: ToDoEntity) = viewModelScope.launch {
        updateToDoListUseCase(todoEntity)
    }

    fun deleteAll() = viewModelScope.launch {
        _toDoListLiveData.postValue(ToDoListState.Loading)
        deleteAllToDoItemUseCase()      // UseCase 호출
        _toDoListLiveData.postValue(ToDoListState.Success(getToDoListUseCase()))       // 비워줌
    }
}