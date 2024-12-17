package vn.edu.hust.studentman

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
  private lateinit var studentAdapter: StudentAdapter
  private val students = mutableListOf(
    StudentModel("Nguyễn Văn An", "SV001"),
    StudentModel("Trần Thị Bảo", "SV002"),
    StudentModel("Lê Hoàng Cường", "SV003"),
    StudentModel("Phạm Thị Dung", "SV004"),
    StudentModel("Đỗ Minh Đức", "SV005"),
    StudentModel("Vũ Thị Hoa", "SV006"),
    StudentModel("Hoàng Văn Hải", "SV007"),
    StudentModel("Bùi Thị Hạnh", "SV008"),
    StudentModel("Đinh Văn Hùng", "SV009"),
    StudentModel("Nguyễn Thị Linh", "SV010"),
    StudentModel("Phạm Văn Long", "SV011"),
    StudentModel("Trần Thị Mai", "SV012"),
    StudentModel("Lê Thị Ngọc", "SV013"),
    StudentModel("Vũ Văn Nam", "SV014"),
    StudentModel("Hoàng Thị Phương", "SV015"),
    StudentModel("Đỗ Văn Quân", "SV016"),
    StudentModel("Nguyễn Thị Thu", "SV017"),
    StudentModel("Trần Văn Tài", "SV018"),
    StudentModel("Phạm Thị Tuyết", "SV019"),
    StudentModel("Lê Văn Vũ", "SV020")
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    studentAdapter = StudentAdapter(students, { student ->
      // Handle edit action
      showEditStudentPopup(student)
    }, { student ->
      // Handle remove action
      removeStudent(student)
    })

    findViewById<RecyclerView>(R.id.recycler_view_students).run {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      showAddStudentPopup()
    }
  }

  private fun showAddStudentPopup() {
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Add New Student")

    val view = layoutInflater.inflate(R.layout.dialog_add_student, null)
    builder.setView(view)

    val studentNameInput = view.findViewById<EditText>(R.id.edit_student_name)
    val studentIdInput = view.findViewById<EditText>(R.id.edit_student_id)

    builder.setPositiveButton("Add") { dialog, _ ->
      val studentName = studentNameInput.text.toString()
      val studentId = studentIdInput.text.toString()
      if (studentName.isNotEmpty() && studentId.isNotEmpty()) {
        addNewStudent(studentName, studentId)
      }
      dialog.dismiss()
    }
    builder.setNegativeButton("Cancel") { dialog, _ ->
      dialog.dismiss()
    }

    builder.create().show()
  }

  private fun addNewStudent(name: String, id: String) {
    val newStudent = StudentModel(name, id)
    students.add(newStudent)
    studentAdapter.notifyItemInserted(students.size - 1)
  }

  private fun showEditStudentPopup(student: StudentModel) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Edit Student")

    val view = layoutInflater.inflate(R.layout.dialog_add_student, null)
    builder.setView(view)

    val studentNameInput = view.findViewById<EditText>(R.id.edit_student_name)
    val studentIdInput = view.findViewById<EditText>(R.id.edit_student_id)

    studentNameInput.setText(student.studentName)
    studentIdInput.setText(student.studentId)

    builder.setPositiveButton("Save") { dialog, _ ->
      val studentName = studentNameInput.text.toString()
      val studentId = studentIdInput.text.toString()
      if (studentName.isNotEmpty() && studentId.isNotEmpty()) {
        student.studentName = studentName
        student.studentId = studentId
        studentAdapter.notifyItemChanged(students.indexOf(student))
      }
      dialog.dismiss()
    }
    builder.setNegativeButton("Cancel") { dialog, _ ->
      dialog.dismiss()
    }

    builder.create().show()
  }

  private var deletedStudent: StudentModel? = null
  private var deletedStudentPosition: Int = -1

  private fun removeStudent(student: StudentModel) {
    deletedStudentPosition = students.indexOf(student)
    deletedStudent = student
    students.removeAt(deletedStudentPosition)
    studentAdapter.notifyItemRemoved(deletedStudentPosition)

    Snackbar.make(findViewById(R.id.main), "Student removed", Snackbar.LENGTH_LONG)
      .setAction("Undo") {
        students.add(deletedStudentPosition, deletedStudent!!)
        studentAdapter.notifyItemInserted(deletedStudentPosition)
      }.show()
  }
}