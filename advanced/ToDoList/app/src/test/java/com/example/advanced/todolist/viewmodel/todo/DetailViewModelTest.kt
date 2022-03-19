package com.example.advanced.todolist.viewmodel.todo

import com.example.advanced.todolist.data.entity.ToDoEntity
import com.example.advanced.todolist.domain.todo.InsertToDoItemUseCase
import com.example.advanced.todolist.presentation.detail.DetailMode
import com.example.advanced.todolist.presentation.detail.DetailViewModel
import com.example.advanced.todolist.presentation.detail.ToDoDetailState
import com.example.advanced.todolist.presentation.list.ListViewModel
import com.example.advanced.todolist.presentation.list.ToDoListState
import com.example.advanced.todolist.viewmodel.ViewModelTest
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.koin.test.inject

/**
 * [DetailViewModel]을 테스트하기 위한 Unit Test Class
 *
 * 1. initData()        : mocking data를 넣어준 다음 잘 불러와지는지 테스트
 * 2. test viewModel fetch          : viewModel에서 fetch 함수를 호출하였을 때, 데이터가 잘 불러와지는지 테스트
 * 3. test delete todo
 * 4. test update todo
 */

internal class DetailViewModelTest: ViewModelTest() {

    private val id = 1L

    private val detailViewModel by inject<DetailViewModel> { parametersOf(DetailMode.DETAIL, id)  }
    private val insertToDoItemUseCase: InsertToDoItemUseCase by inject ()

    private val todo = ToDoEntity(
        id = id,
        title = "title $id",
        description = "description $id",
        hasCompleted = false
    )

    @Before
    fun init() {
        initData()
    }

    private fun initData() = runBlockingTest {      //  runBlockingTest :: coroutine
        insertToDoItemUseCase(todo)     // mockList data로 데이터 초기화
    }

    // Test : 입력된 데이터를 불러와서 검증
    @Test
    fun `test viewModel fetch`(): Unit = runBlockingTest {
        val testObservable = detailViewModel.todoDetailLiveData.test()
        detailViewModel.fetchData()       // 데이터를 불러오면
        testObservable.assertValueSequence(         //  testObservable에서 검증했던 데이터가 무엇이 있나 확인
            listOf(        //  순서대로 가져옴
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Success(todo)
            )
        )
    }
}