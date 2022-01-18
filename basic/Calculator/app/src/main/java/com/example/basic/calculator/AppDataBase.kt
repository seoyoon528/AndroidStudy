package com.example.basic.calculator

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.basic.calculator.dao.HistoryDao
import com.example.basic.calculator.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao       //  AppDataBase 생성 시 historyDao를 사용할 수 있게함
}