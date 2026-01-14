package com.example.studentsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.studentsapp.model.Model
import com.example.studentsapp.model.Student

class StudentDetailsActivity : AppCompatActivity() {

    private lateinit var tvStudentName: TextView
    private lateinit var tvStudentId: TextView
    private lateinit var tvStudentPhone: TextView
    private lateinit var tvStudentAddress: TextView
    private lateinit var cbChecked: CheckBox
    private var studentId: String = ""
    private var student: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        // Setup Toolbar with back button
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize Views
        tvStudentName = findViewById(R.id.tvStudentName)
        tvStudentId = findViewById(R.id.tvStudentId)
        tvStudentPhone = findViewById(R.id.tvStudentPhone)
        tvStudentAddress = findViewById(R.id.tvStudentAddress)
        cbChecked = findViewById(R.id.cbChecked)

        // Get student ID from the Intent
        studentId = intent.getStringExtra("student_id") ?: ""

        // Populate details
        refreshStudentDetails()
        // Handle Edit Button Click
        findViewById<Button>(R.id.btnEdit).setOnClickListener {
            val intent = Intent(this, EditStudentActivity::class.java).apply {
                putExtra("student_id", studentId)
            }
            startActivity(intent)
        }

        cbChecked.setOnClickListener {
            student?.isChecked = cbChecked.isChecked
            if (student != null) {
                Model.shared.updateStudent(studentId, student!!) { }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshStudentDetails()
    }

    private fun refreshStudentDetails() {
        Model.shared.getStudentById(studentId) { student ->
            if (student != null) {
                this.student = student
                tvStudentName.text = "Name: ${student.name}"
                tvStudentId.text = "ID: ${student.id}"
                tvStudentPhone.text = "Phone: ${student.phone}"
                tvStudentAddress.text = "Address: ${student.address}"
                cbChecked.isChecked = student.isChecked

                studentId = student.id
            } else {
                // Handle case where student no longer exists
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // Navigate back
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}