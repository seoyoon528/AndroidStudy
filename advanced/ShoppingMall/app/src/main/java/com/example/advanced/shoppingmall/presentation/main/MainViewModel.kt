package com.example.advanced.shoppingmall.presentation.main

import androidx.lifecycle.viewModelScope
import com.example.advanced.shoppingmall.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class MainViewModel: BaseViewModel() {

    override fun fetchData(): Job = viewModelScope.launch {  }      //  Coroutine
}