package com.example.studentsapp.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.studentsapp.model.Student

@Dao
interface StudentDao {

    @Query("SELECT * FROM Student")
    fun getAllStudent(): List<Student>

    @Query("SELECT * FROM Student WHERE id =:id")
    fun getStudentById(id: String): Student

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg student: Student)

    @Query("DELETE FROM Student WHERE id = :id")
    fun deleteStudentById(id: String)

    @Query("DELETE FROM Student")
    fun deleteAll()
}