package com.example.advanced.shoppingmall.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.advanced.shoppingmall.data.entity.product.ProductEntity
import com.example.advanced.shoppingmall.domain.GetProductItemUseCase
import com.example.advanced.shoppingmall.domain.GetProductListUseCase
import com.example.advanced.shoppingmall.domain.OrderProductItemUseCase
import com.example.advanced.shoppingmall.presentation.BaseViewModel
import com.example.advanced.shoppingmall.presentation.list.ProductListState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ProductDetailViewModel(
    private val productId: Long,
    private val getProductItemUseCase: GetProductItemUseCase,
    private val orderProductItemUseCase: OrderProductItemUseCase
) : BaseViewModel() {

    // LiveData
    private var _productDetailStateLiveData = MutableLiveData<ProductDetailState>(ProductDetailState.UnInitialized)
    val productDetailStateLiveData: LiveData<ProductDetailState> = _productDetailStateLiveData

    private lateinit var productEntity: ProductEntity

    override fun fetchData(): Job = viewModelScope.launch {
        setState(ProductDetailState.Loading)

        getProductItemUseCase(productId)?.let { product ->          // productID에 해당하는 entity가 있는 경우
            productEntity = product             //  객체를 가져다가 사용
            setState (
                ProductDetailState.Success(product)
            )
        } ?: kotlin.run {
            setState(ProductDetailState.Error)
        }
    }

    fun orderProduct() = viewModelScope.launch {
        if (::productEntity.isInitialized) {            // product entity가 초기화 되었다면
            val productId = orderProductItemUseCase(productEntity)
            if (productEntity.id == productId) {
                setState(ProductDetailState.Order)            // 주문
            }
        } else {            // 초기화 안되어있다면
            setState(ProductDetailState.Error)          // 에러
        }
    }

    private fun setState(state: ProductDetailState) {
        _productDetailStateLiveData.postValue(state)
    }
}