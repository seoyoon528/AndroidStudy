package com.example.advanced.todolist.viewmodel.todo

import com.example.advanced.todolist.data.entity.ToDoEntity
import com.example.advanced.todolist.domain.todo.GetToDoItemUseCase
import com.example.advanced.todolist.domain.todo.InsertToDoListUseCase
import com.example.advanced.todolist.presentation.list.ListViewModel
import com.example.advanced.todolist.presentation.list.ToDoListState
import com.example.advanced.todolist.viewmodel.ViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.koin.core.inject

/**
 * [ListViewModel]을 테스트하기 위한 Unit Test Class
 *
 * 1. initData()        : mocking data를 넣어준 다음 잘 불러와지는지 테스트
 * 2. test viewModel fetch          : viewModel에서 fetch 함수를 호출하였을 때, 데이터가 잘 불러와지는지 테스트
 * 3. test Item update          : 불러온 데이터가 잘 update 되는지 테스트
 * 4. test Item delete all          : 아이템이 전체 삭제 되는지
 */

@ExperimentalCoroutinesApi
internal class ListViewModelTest: ViewModelTest() {

    private val viewModel: ListViewModel by inject()

    private val insertToDoListUseCase: InsertToDoListUseCase by inject()
    private val getToDoItemUseCase: GetToDoItemUseCase by inject()

    private val mockList = (0 until 10).map {
        ToDoEntity (
            id = it.toLong(),
            title = "title $it",
            description = "description $it",
            hasCompleted = false
        )
    }

    @Before
    fun init() {
        initData()
    }

    private fun initData() = runBlockingTest {      //  runBlockingTest :: coroutine
        insertToDoListUseCase(mockList)     // mockList data로 데이터 초기화
    }

    // Test : 입력된 데이터를 불러와서 검증
    @Test
    fun `test viewModel fetch`(): Unit = runBlockingTest  {
        val testObservable = viewModel.todoListLiveData.test()
        viewModel.fetchData()       // 데이터를 불러오면
        testObservable.assertValueSequence(         //  testObservable에서 검증했던 데이터가 무엇이 있나 확인
            listOf (
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(mockList)     // mockList를 불러오면 Success
            )
        )
    }

    // Test : 데이터를 업데이트 하였을 때 잘 반영되는지 검증
    @Test
    fun `test Item update`(): Unit = runBlockingTest {
        val todo = ToDoEntity (
            id = 1,
            title = "title 1",
            description = "description 1",
            hasCompleted = true
        )
        viewModel.updateEntity(todo)
        assert(getToDoItemUseCase(todo.id)?.hasCompleted ?: false ==  todo.hasCompleted)        // 가져온 값과 반영된 값이 같은지 판단
    }

    // Test : 데이터를 모두 삭제했을 때 빈 상태로 보여지는지 검증
    @Test
    fun `test Item delete all`(): Unit = runBlockingTest {
        val testObservable = viewModel.todoListLiveData.test()
        viewModel.deleteAll()
        testObservable.assertValueSequence(
            listOf (
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf())     // 비워져 있는 listOf
            )
        )
    }
}