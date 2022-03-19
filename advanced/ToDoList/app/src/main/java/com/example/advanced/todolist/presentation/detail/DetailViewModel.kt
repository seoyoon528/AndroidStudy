package com.example.advanced.todolist.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.advanced.todolist.domain.todo.GetToDoItemUseCase
import com.example.advanced.todolist.presentation.list.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class DetailViewModel(

    var detailMode: DetailMode,
    var id: Long = -1,      // 이상한 값 들어가는 것 방지
    private val getToDoItemUseCase: GetToDoItemUseCase      //  상세화면의 정보 불러옴

    ): BaseViewModel() {

    private var _toDoDetailLiveData = MutableLiveData<ToDoDetailState>(ToDoDetailState.UnInitialized)
    val todoDetailLiveData: LiveData<ToDoDetailState> = _toDoDetailLiveData


    override fun fetchData(): Job = viewModelScope.launch{
        when (detailMode) {
            DetailMode.WRITE -> {
                //TODO  작성 모드로 상세화면 진입 로직 처리
            }
            DetailMode.DETAIL -> {
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

}