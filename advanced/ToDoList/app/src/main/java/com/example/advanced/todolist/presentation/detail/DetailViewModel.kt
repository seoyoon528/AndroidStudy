package com.example.advanced.todolist.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.advanced.todolist.data.entity.ToDoEntity
import com.example.advanced.todolist.domain.todo.DeleteToDoItemUseCase
import com.example.advanced.todolist.domain.todo.GetToDoItemUseCase
import com.example.advanced.todolist.domain.todo.InsertToDoItemUseCase
import com.example.advanced.todolist.domain.todo.UpdateToDoListUseCase
import com.example.advanced.todolist.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class DetailViewModel(

    var detailMode: DetailMode,
    var id: Long = -1,      // 이상한 값 들어가는 것 방지
    private val getToDoItemUseCase: GetToDoItemUseCase,      //  상세화면의 정보 불러옴
    private val deleteToDoItemUseCase: DeleteToDoItemUseCase,        // 아이템 삭제
    private val updateToDoUseCase: UpdateToDoListUseCase,
    private val insertToDoItemUseCase: InsertToDoItemUseCase

    ): BaseViewModel() {

    private var _toDoDetailLiveData = MutableLiveData<ToDoDetailState>(ToDoDetailState.UnInitialized)
    val todoDetailLiveData: LiveData<ToDoDetailState> = _toDoDetailLiveData

    fun setModifyMode() = viewModelScope.launch {
        _toDoDetailLiveData.postValue(ToDoDetailState.Modify)
    }


    override fun fetchData(): Job = viewModelScope.launch{
        when (detailMode) {     // Mode에 따라 분기처리
            DetailMode.WRITE -> {
                _toDoDetailLiveData.postValue(ToDoDetailState.Write)      // Write Mode
            }

            DetailMode.DETAIL -> {      // Detail Mode
                _toDoDetailLiveData.postValue(ToDoDetailState.Loading)
                try {
                    getToDoItemUseCase(id)?.let {
                        _toDoDetailLiveData.postValue(ToDoDetailState.Success(it))      //  데이터가 성공적으로 불러와진 경우 Success
                    } ?: kotlin.run {
                        _toDoDetailLiveData.postValue(ToDoDetailState.Error)        // todoItem 정보가 null인 경우
                    }
                } catch (e : Exception) {
                    e.printStackTrace()
                    _toDoDetailLiveData.postValue(ToDoDetailState.Error)
                }
            }
        }
    }

    fun deleteTodo() = viewModelScope.launch {
        _toDoDetailLiveData.postValue(ToDoDetailState.Loading)

        try {
            deleteToDoItemUseCase(id)
            _toDoDetailLiveData.postValue(ToDoDetailState.Delete)       // ToDoDetailState = Delete
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            _toDoDetailLiveData.postValue(ToDoDetailState.Error)
        }
    }

    fun writeToDo(title: String, description: String) = viewModelScope.launch {
        _toDoDetailLiveData.postValue(ToDoDetailState.Loading)

        when (detailMode) {
            DetailMode.WRITE -> {
                try {
                    val toDoEntity = ToDoEntity(
                        title = title,
                        description = description
                    )
                    id = insertToDoItemUseCase(toDoEntity)
                    _toDoDetailLiveData.postValue(ToDoDetailState.Success(toDoEntity))
                    detailMode = DetailMode.DETAIL

                } catch (e: Exception) {
                    e.printStackTrace()
                    _toDoDetailLiveData.postValue(ToDoDetailState.Error)
                }
            }

            DetailMode.DETAIL -> {
                _toDoDetailLiveData.postValue(ToDoDetailState.Loading)
                try {
                    getToDoItemUseCase(id)?.let {       // 아이템을 가져와서
                        val updateToDoEntity = it.copy(        // updateToDoEntity에 복사해줌
                            title = title,
                            description = description
                        )
                        updateToDoUseCase(updateToDoEntity)     // updateToDoEntity를 UseCase에 넣어줌
                        _toDoDetailLiveData.postValue(ToDoDetailState.Success(updateToDoEntity))      //  데이터가 성공적으로 업데이트 된 경우 Success
                    } ?: kotlin.run {
                        _toDoDetailLiveData.postValue(ToDoDetailState.Error)        // id가 없는 경우
                    }
                } catch (e : Exception) {
                    e.printStackTrace()
                    _toDoDetailLiveData.postValue(ToDoDetailState.Error)
                }
            }
        }
    }
}