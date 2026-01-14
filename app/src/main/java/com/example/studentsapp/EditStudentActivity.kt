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

class EditStudentActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etId: EditText
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var cbChecked: CheckBox
    private var studentId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student)

        // Setup Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Edit Student"

        // Initialize Views
        etName = findViewById(R.id.etEditName)
        etId = findViewById(R.id.etEditId)
        etPhone = findViewById(R.id.etEditPhone)
        etAddress = findViewById(R.id.etEditAddress)
        cbChecked = findViewById(R.id.cbEditChecked)

        val btnSave: Button = findViewById(R.id.btnEditSave)
        val btnDelete: Button = findViewById(R.id.btnEditDelete)
        val btnCancel: Button = findViewById(R.id.btnEditCancel)

        // Get the student ID passed from the Details Activity
        studentId = intent.getStringExtra("student_id") ?: ""

        // Load existing student data
        loadStudentData()

        // SAVE logic
        btnSave.setOnClickListener {
            saveStudentChanges()
        }

        // DELETE logic
        btnDelete.setOnClickListener {
            deleteStudent()
        }

        // CANCEL logic
        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun loadStudentData() {
        Model.shared.getStudentById(studentId) { student ->
            student?.let {
                etName.setText(it.name)
                etId.setText(it.id)
                etPhone.setText(it.phone)
                etAddress.setText(it.address)
                cbChecked.isChecked = it.isChecked
            }
        }
    }

    private fun saveStudentChanges() {
        val name = etName.text.toString().trim()
        val newId = etId.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val address = etAddress.text.toString().trim()
        val isChecked = cbChecked.isChecked

        if (name.isEmpty() || newId.isEmpty()) {
            Toast.makeText(this, "Name and ID are required", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedStudent = Student(newId, name, phone, address, isChecked, "")

        // Use the original studentId to find the record, but update with new values
        Model.shared.updateStudent(studentId, updatedStudent) {
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show()
            finish() // Return to details page
        }
    }

    private fun deleteStudent() {
        // Calling the correct method name from your Model
        Model.shared.deleteStudentById(studentId) { success ->
            if (success) {
                Toast.makeText(this, "Student deleted", Toast.LENGTH_SHORT).show()
                // Closing this activity returns you to StudentDetailsActivity.
                // Since that activity calls refreshStudentDetails() in onResume,
                // it will find that the student is gone and finish() itself automatically.
                finish()
            } else {
                Toast.makeText(this, "Error: Could not delete student", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}