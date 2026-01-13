package com.example.studentsapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentsapp.model.Model
import com.example.studentsapp.model.Student
import com.google.android.material.floatingactionbutton.FloatingActionButton

interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class StudentsRecyclerViewActivity : AppCompatActivity() {

    private lateinit var adapter: StudentsRecyclerAdapter
    private var students: List<Student> = listOf() // Immutable list for DAO compatibility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_recycler_view)

        // Setup Toolbar
        val toolbar: Toolbar = findViewById(R.id.students_recycler_view_main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Students List"

        // Edge-to-edge insets setup
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()

        // Fetch and display students
        loadStudents()

        // Setup Floating Action Button for adding a new student
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.students_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = StudentsRecyclerAdapter(students) { student ->
            navigateToDetails(student)
        }
        recyclerView.adapter = adapter
    }

    private fun loadStudents() {
        Model.shared.getAllStudents { result ->
            result?.let {
                students = it
                adapter.updateStudents(students)
            } ?: run {
                Toast.makeText(this, "Error loading students", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToDetails(student: Student) {
        val intent = Intent(this, StudentDetailsActivity::class.java).apply {
            putExtra("student_id", student.id)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        loadStudents() // Refresh data on resume
    }
}

class StudentsRecyclerAdapter(
    private var students: List<Student>,
    private val onItemClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentsRecyclerAdapter.StudentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_list_row, parent, false)
        return StudentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student)
        holder.itemView.setOnClickListener { onItemClick(student) }
        holder.studentCheckBox.setOnClickListener {
            student.isChecked = holder.studentCheckBox.isChecked
            Model.shared.updateStudent(student.id, student) { }
        }
    }

    override fun getItemCount(): Int = students.size

    fun updateStudents(newStudents: List<Student>) {
        students = newStudents
        notifyDataSetChanged()
    }

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.student_row_name_text_view)
        private val idTextView: TextView = itemView.findViewById(R.id.student_row_id_text_view)
        val studentCheckBox: CheckBox = itemView.findViewById(R.id.student_row_check_box)

        fun bind(student: Student) {
            nameTextView.text = student.name
            idTextView.text = student.id
            studentCheckBox.isChecked = student.isChecked
        }
    }
}
