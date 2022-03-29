package com.example.advanced.shoppingmall.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.advanced.shoppingmall.domain.GetProductListUseCase
import com.example.advanced.shoppingmall.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ProductListViewModel(
    private val  getProductListUseCase: GetProductListUseCase
): BaseViewModel() {

    // LiveData
    private var _productListStateLiveData = MutableLiveData<ProductListState>(ProductListState.UnInitialized)
    val productListStateLiveData: LiveData<ProductListState> = _productListStateLiveData

    //  Coroutine Block
    override fun fetchData(): Job = viewModelScope.launch {
        setState(
            ProductListState.Loading
        )
        setState(
            ProductListState.Success(           //  데이터를 불러줄 때 Success
                getProductListUseCase()
            )
        )
    }

    private fun setState(state: ProductListState) {
        _productListStateLiveData.postValue(state)
    }
}