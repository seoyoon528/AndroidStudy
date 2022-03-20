package com.example.advanced.todolist.viewmodel.todo

import com.example.advanced.todolist.data.entity.ToDoEntity
import com.example.advanced.todolist.presentation.detail.DetailMode
import com.example.advanced.todolist.presentation.detail.DetailViewModel
import com.example.advanced.todolist.presentation.detail.ToDoDetailState
import com.example.advanced.todolist.presentation.list.ListViewModel
import com.example.advanced.todolist.presentation.list.ToDoListState
import com.example.advanced.todolist.viewmodel.ViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject

/**
 * [DetailViewModelForWriteTest]을 테스트하기 위한 Unit Test Class
 *
 * 1. test viewModel fetch
 * 2. test insert todo
 *
 */

@ExperimentalCoroutinesApi
internal class DetailViewModelForWriteTest: ViewModelTest() {

    private val id = 0L

    private val detailViewModel by inject<DetailViewModel> { parametersOf(DetailMode.WRITE, id) }
    private val listViewModel by inject<ListViewModel>()

    private val todo = ToDoEntity(
        id = id,
        title = "title $id",
        description = "description $id",
        hasCompleted = false
    )

    @Test
    fun `test viewModel fetch`(): Unit = runBlockingTest {
        val testObservable =
            detailViewModel.todoDetailLiveData.test()      // todoDetailLiveData의 test를 가져옴

        detailViewModel.fetchData()       // 데이터를 불러오면

        testObservable.assertValueSequence(
            listOf(        //  순서대로 가져옴
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Write
            )
        )
    }

    @Test
    fun `test insert todo`(): Unit = runBlockingTest {
        val detailTestObservable =
            detailViewModel.todoDetailLiveData.test()      // todoDetailLiveData의 test를 가져옴
        val listTestObservable =
            listViewModel.todoListLiveData.test()      // todoListLiveData의 test를 가져옴

        detailViewModel.writeToDo(
            title = todo.title,
            description = todo.description
        )

        detailTestObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Success(todo)
            )
        )

        assert(detailViewModel.detailMode == DetailMode.DETAIL)         // 확인
        assert(detailViewModel.id == id)

        // 뒤로 나가서 리스트 보기
        listViewModel.fetchData()
        listTestObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(
                    listOf(
                        todo
                    )
                )
            )
        )
    }
}