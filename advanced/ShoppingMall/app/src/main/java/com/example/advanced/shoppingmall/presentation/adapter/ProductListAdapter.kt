package com.example.advanced.shoppingmall.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.advanced.shoppingmall.data.entity.product.ProductEntity
import com.example.advanced.shoppingmall.databinding.ViewholderProductItemBinding
import com.example.advanced.shoppingmall.extensions.loadCenterCrop

class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.ProductItemViewHolder>() {

    private var productList: List<ProductEntity> = listOf()
    private lateinit var productItemClickListener: (ProductEntity) -> Unit

    inner class ProductItemViewHolder(
        private val binding: ViewholderProductItemBinding,
        val productItemClickListener: (ProductEntity) -> Unit           //  Entity를 함수형 타입으로 넘겨
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(data: ProductEntity) = with(binding) {             // Entity의 데이터를 Set하고
            productNameTextView.text = data.productName
            productImageView.loadCenterCrop(data.productImage, 8f)
            productPriceTextView.text = "${data.productPrice}원"
        }

        fun bindViews(data: ProductEntity) {            // 처리함
            binding.root.setOnClickListener {
                productItemClickListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val view = ViewholderProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductItemViewHolder(view, productItemClickListener)
    }

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        holder.bindData(productList[position])
        holder.bindViews(productList[position])
    }

    override fun getItemCount(): Int = productList.size

    fun setProductList(productList: List<ProductEntity>, productItemClickListener: (ProductEntity) -> Unit = { }) {             // Unit = {}  :: 상품 리스트 뿐만 아니라 프로필에서도 자신의 주문 목록을 확인할 수 있게하기 위해 넣음
        this.productList = productList
        this.productItemClickListener = productItemClickListener
        notifyDataSetChanged()
    }
}