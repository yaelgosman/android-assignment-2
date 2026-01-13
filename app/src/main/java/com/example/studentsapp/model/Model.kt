package com.example.studentsapp.model

import com.example.studentsapp.model.dao.AppLocalDb
import com.example.studentsapp.model.dao.AppLocalDbRepository
import com.example.studentsapp.model.dao.StudentDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Model private constructor() {

    private val database: AppLocalDbRepository = AppLocalDb.database
    private val studentDao: StudentDao = database.studentDao()

    companion object {
        val shared = Model()
    }

    init {
        clearAllStudentsOnInit()
    }

    private fun clearAllStudentsOnInit() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                studentDao.deleteAll()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAllStudents(callback: (List<Student>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val students = studentDao.getAllStudent()
                withContext(Dispatchers.Main) {
                    callback(students)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback(null)
                }
            }
        }
    }

    fun getStudentById(id: String, callback: (Student?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val student = studentDao.getStudentById(id)
                withContext(Dispatchers.Main) {
                    callback(student)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback(null)
                }
            }
        }
    }


    fun deleteStudentById(id: String, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                studentDao.deleteStudentById(id)
                withContext(Dispatchers.Main) {
                    callback(true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback(false)
                }
            }
        }
    }

    fun addStudent(student: Student, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val existingStudent = studentDao.getStudentById(student.id)
                if (existingStudent == null) {
                    studentDao.insertAll(student)
                    withContext(Dispatchers.Main) {
                        callback(true)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        callback(false)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback(false)
                }
            }
        }
    }

    fun updateStudent(originalStudentId: String, student: Student, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val existingStudent = studentDao.getStudentById(originalStudentId)
                if (existingStudent == null) {
                    withContext(Dispatchers.Main) {
                        callback(false)
                    }
                } else {
                    studentDao.deleteStudentById(originalStudentId)
                    studentDao.insertAll(student)
                    withContext(Dispatchers.Main) {
                        callback(true)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback(false)
                }
            }
        }
    }
}