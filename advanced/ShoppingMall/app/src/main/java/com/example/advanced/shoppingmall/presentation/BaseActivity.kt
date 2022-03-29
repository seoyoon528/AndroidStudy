package com.example.advanced.shoppingmall.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job

internal abstract class BaseActivity<VM: BaseViewModel, VB: ViewBinding>: AppCompatActivity() {         // BaseViewModel과 ViewBinding 상속받음

    abstract val viewModel: VM          // ViewModel을 추상 변수로 두어 하위에서 구현할 수 있도록 함

    protected lateinit var binding: VB

    abstract fun getViewBinding(): VB

    private lateinit var fetchJob: Job          // ViewModel에서

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        fetchJob = viewModel.fetchData()            // ViewModel Scope에서 코루틴 블록을 실행시키면 그거에 대한 job instance를 fetchJob이라는 이름으로 반환 받음
        observeData()
    }

    abstract fun observeData()          //  추상 함수로 알아서 구현하게 함 (liveData 이용할 때 주로 사용)

    override fun onDestroy() {
        if (fetchJob.isActive) {
            fetchJob.cancel()
        }
        super.onDestroy()
    }
}