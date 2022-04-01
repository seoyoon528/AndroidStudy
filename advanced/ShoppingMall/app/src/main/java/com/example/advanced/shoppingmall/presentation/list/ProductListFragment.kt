package com.example.advanced.shoppingmall.presentation.list

import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import com.example.advanced.shoppingmall.databinding.FragmentProductListBinding
import com.example.advanced.shoppingmall.extensions.toast
import com.example.advanced.shoppingmall.presentation.BaseFragment
import com.example.advanced.shoppingmall.presentation.adapter.ProductListAdapter
import com.example.advanced.shoppingmall.presentation.detail.ProductDetailActivity
import com.example.advanced.shoppingmall.presentation.main.MainActivity
import org.koin.android.ext.android.inject

internal class ProductListFragment: BaseFragment<ProductListViewModel, FragmentProductListBinding>() {

    companion object {
        const val TAG = "ProductListFragment"
    }

    override val viewModel by inject<ProductListViewModel>()

    override fun getViewBinding(): FragmentProductListBinding = FragmentProductListBinding.inflate(layoutInflater)

    private val adapter =  ProductListAdapter()

    private val startProductDetailForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == ProductDetailActivity.PRODUCT_ORDERED_RESULT_CODE) {
            (requireActivity() as MainActivity).viewModel.refreshOrderList()
        }
    }

    override fun observeData() = viewModel.productListStateLiveData.observe(this){          // ProductListViewModel의 LiveData 구독해서 State 값에 따라 처리

        when(it) {
            is ProductListState.UnInitialized -> {
                initViews(binding)
            }
            is ProductListState.Loading -> {
                handleLoadingState()
            }
            is ProductListState.Success -> {
                handleSuccessState(it)
            }
            is ProductListState.Error -> {
                handleErrorState()
            }

        }
    }

    private fun initViews(binding: FragmentProductListBinding) = with(binding) {
        recyclerView.adapter = adapter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }
    }

    // Loading 중인 상태
    private fun handleLoadingState() = with(binding) {
        refreshLayout.isRefreshing = true
    }

    private fun handleSuccessState(state: ProductListState.Success) = with(binding) {
        refreshLayout.isRefreshing = false //  Loading중인 상태가 없어지도록

        if (state.productList.isEmpty()) {          // ProductList가 없다면
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        } else {            // 있다면
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.setProductList(state.productList) {
                startProductDetailForResult.launch(         // 실행시켜 intent를 콜백으로 받음
                    ProductDetailActivity.newIntent(requireContext(), it.id)
                )
                requireContext().toast("Product Entity: $it")
            }
        }
    }

    private fun handleErrorState() {
        Toast.makeText(requireContext(), "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
    }

}