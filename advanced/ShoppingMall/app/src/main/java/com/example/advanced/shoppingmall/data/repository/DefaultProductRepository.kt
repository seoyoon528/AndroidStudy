package com.example.advanced.shoppingmall.data.repository

import com.example.advanced.shoppingmall.data.db.dao.ProductDao
import com.example.advanced.shoppingmall.data.entity.product.ProductEntity
import com.example.advanced.shoppingmall.data.network.ProductApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultProductRepository (        //  주입 받을 것들
    private val productApi: ProductApiService,
    private val productDao: ProductDao,
    private val ioDispatcher: CoroutineDispatcher             // API를 기반으로 불러와야하기 때문에 Coroutine 사용
): ProductRepository {

    // Repository 구현체
    override suspend fun getProductList(): List<ProductEntity> = withContext(ioDispatcher) {
        val response = productApi.getProducts()
        return@withContext if (response.isSuccessful) {         // 만약 response가 success라면
            response.body()?.items?.map { it.toEntity() } ?: listOf()           // 해당 response의 body(item)를 꺼냄
        } else {            // 만약 null 값이라면
            listOf()
        }
    }

    override suspend fun getLocalProductList(): List<ProductEntity> = withContext(ioDispatcher) {
        productDao.getAll()
    }

    override suspend fun insertProductItem(ProductItem: ProductEntity): Long = withContext(ioDispatcher) {
        productDao.insert(ProductItem)
    }

    override suspend fun insertProductList(ProductList: List<ProductEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProductItem(ProductItem: ProductEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getProductItem(itemId: Long): ProductEntity? = withContext(ioDispatcher) {
        val response = productApi.getProduct(itemId)
        return@withContext if (response.isSuccessful) {
            response.body()?.toEntity()
        } else {
            null
        }
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        productDao.deleteAll()
    }

    override suspend fun deleteProductItem(id: Long) {
        TODO("Not yet implemented")
    }

}