package com.example.advanced.todolist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Job

internal abstract class BaseActivity<ViewModel: BaseViewModel>: AppCompatActivity()  {

    abstract val viewModel: ViewModel

    private lateinit var fetchJob: Job

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

        fetchJob = viewModel.fetchData()        // onCreate될 때마다 data fetch
        observeData()
    }

    abstract fun observeData()          // 이 함수가 호출되는 시점에 구독한 상태 값에 따라 UI가 변경됨

    override fun onDestroy() {
        if (fetchJob.isActive) {
            fetchJob.cancel()           //  비동기 처리 취소
        }
        super.onDestroy()
    }
}