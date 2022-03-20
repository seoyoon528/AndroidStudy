package com.example.advanced.todolist.data.local.db.dao

import androidx.room.*
import com.example.advanced.todolist.data.entity.ToDoEntity

@Dao
interface ToDoDao {

    @Query ("SELECT * FROM ToDoEntity")
    suspend fun getAll(): List<ToDoEntity>

    @Query ("SELECT * FROM ToDoEntity WHERE id=:id")
    suspend fun getById(id: Long): ToDoEntity?

    @Insert
    suspend fun insert(toDoEntity: ToDoEntity): Long

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(toDoEntityList: List<ToDoEntity>)

    @Query ("DELETE FROM ToDoEntity WHERE id=:id")
    suspend fun delete(id: Long): Boolean

    @Query ("DELETE FROM ToDoEntity")
    suspend fun deleteAll()

    @Update
    suspend fun update(toDoEntity: ToDoEntity): Boolean

}