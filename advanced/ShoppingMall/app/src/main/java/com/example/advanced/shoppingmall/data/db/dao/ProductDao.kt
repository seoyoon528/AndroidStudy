package com.example.advanced.shoppingmall.data.db.dao

import com.example.advanced.shoppingmall.data.entity.product.ProductEntity

//@Dao
//interface  ProductDao {
//    @Query("SELECT * FROM ProductEntity")
//    suspend fun getAll(): List<ProductEntity>
//
//    @Query("SELECT * FROM ProductEntity WHERE id=:id")
//    suspend fun getById(id: Long): ProductEntity?
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(ProductEntity: ProductEntity): Long
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(ProductEntityList: List<ProductEntity>)
//
//    @Query("DELETE FROM ProductEntity WHERE id=:id")
//    suspend fun delete(id: Long)
//
//    @Query("DELETE FROM ProductEntity")
//    suspend fun deleteAll()
//
//    @Update
//    suspend fun update(ProductEntity: ProductEntity)
//}