package com.example.advanced.shoppingmall.data.db.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.advanced.shoppingmall.data.entity.product.ProductEntity
import com.example.advanced.shoppingmall.utility.DateConverter

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class ProductDatabase: RoomDatabase() {

    companion object {
        const val DB_NAME = "ProductDatabase.db"
    }

    abstract fun productDao(): ProductDao

}