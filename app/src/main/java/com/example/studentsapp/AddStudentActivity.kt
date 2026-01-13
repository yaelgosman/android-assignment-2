package com.example.studentsapp

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.studentsapp.model.Model
import com.example.studentsapp.model.Student

class AddStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        // Set up Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Find views
        val etName: EditText = findViewById(R.id.etName)
        val etId: EditText = findViewById(R.id.etId)
        val etPhone: EditText = findViewById(R.id.etPhone)
        val etAddress: EditText = findViewById(R.id.etAddress)
        val cbChecked: CheckBox = findViewById(R.id.cbChecked)
        val btnSave: Button = findViewById(R.id.btnSave)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        // Handle Save button click
        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val id = etId.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val address = etAddress.text.toString().trim()
            val isChecked = cbChecked.isChecked

            // Validate input
            if (name.isEmpty() || id.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newStudent = Student(id, name, phone, address, isChecked, "")
            Model.shared.addStudent(newStudent) { success ->
                if (success) {
                    Toast.makeText(this@AddStudentActivity, "Student added successfully", Toast.LENGTH_SHORT).show()
                    // Close the activity
                    finish()
                } else {
                    Toast.makeText(this@AddStudentActivity, "A student with this ID already exists", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Handle Cancel button click
        btnCancel.setOnClickListener {
            // Simply close the activity
            finish()
        }
    }

    // Handle back button press in the toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // Close the activity when the back button is pressed
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}