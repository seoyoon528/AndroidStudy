package com.example.advanced.todolist.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.advanced.todolist.di.AppTestModule
import com.example.advanced.todolist.livedata.LiveDataTestObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi          // 코루틴 API를 공통으로 사용하기 위해 (setMain, resetMain)
internal abstract class ViewModelTest: KoinTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var context: Application

    private val dispatcher = TestCoroutineDispatcher()      // thread를 쉽게 바꿀 수 있도록 dispatcher 정의

    @Before
    fun setup() {
        startKoin {
            androidContext(context)
            modules(AppTestModule)
        }
        Dispatchers.setMain(dispatcher)
    }

    // 테스트가 끝난 후에는 코루틴을 사용할 것이기 때문에 주입했던 컴포넌트(Koin) 들을 마무리함
    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()     // MainDispatcher를 초기화 해주어야 메모리 누수가 발생하지 않음
    }

    protected fun <T> LiveData<T>.test(): LiveDataTestObserver<T> {         //  LiveData 내부에 어떠한 인스턴스를 담을 수 있는 형태로 만들기 위해 generic. // LiveDataTestObserver를 반환함으로써 들어온 인스턴스를 검증
        val testObserver = LiveDataTestObserver<T> ()
        observeForever(testObserver)        // 안드로이드 Lifecycle에 있는 LiveData 함수 넣어줌

        return testObserver
    }


}