package com.example.basic.calculator.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.basic.calculator.model.History

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")
    fun getAll(): List<History>     // History entity를 전부 가져옴

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM history")
    fun deleteAll()

//    @Delete
//    fun delete(history: History)

//    @Query("SELECT * FROM history WHERE result LIKE :result")
//    fun findByResult(result: String)
}