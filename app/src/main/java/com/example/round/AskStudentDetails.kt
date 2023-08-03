package com.example.round

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AskStudentDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_student_details)

        val name = findViewById<EditText>(R.id.etStudentName)
        val age = findViewById<EditText>(R.id.etStudentAge)
        val dept = findViewById<EditText>(R.id.etStudentDept)
        val year = findViewById<EditText>(R.id.etStudentYear)
        val dob = findViewById<EditText>(R.id.etStudentDOB)
        val regno = findViewById<EditText>(R.id.etStudentRegno)
        val btnsubmit = findViewById<Button>(R.id.btnStudentSubmit)
        val btnskip = findViewById<Button>(R.id.btnSkip)

        btnskip.setOnClickListener {
            Intent(this, StudentsActivity :: class.java).also{
                startActivity(it)
                finish()
            }
        }

        val cache = getSharedPreferences("Cache", MODE_PRIVATE)
        val currentuser = cache.getString("Username", "").toString().lowercase()
        val sharedpref = getSharedPreferences("Students", MODE_PRIVATE)
        val editor = sharedpref.edit()



        btnsubmit.setOnClickListener {
            val data = regno.text.toString().lowercase()
            val validity = isDetailsValid(name.text.toString(),
                age.text.toString(),
                dept.text.toString(),
                year.text.toString(),
                dob.text.toString(),
                regno.text.toString())

            Log.d("AskActivity", "Name : ${name.text.toString().length}," +
                    "Age : ${age.text.toString().length}" +
                    "Dept : ${dept.text.toString().length}" +
                    "Year : ${year.text.toString().length}" +
                    "DOB : ${dob.text.toString().length}" +
                    "Reg no : ${regno.text.toString().length}")
            Log.d("AskActivity", validity.toString())
            if(validity){
                editor.apply {
                    putString("Name" + data + currentuser,name.text.toString())
                    putString("Age" + data + currentuser,age.text.toString())
                    putString("Dept" + data + currentuser,dept.text.toString())
                    putString("Year" + data + currentuser,year.text.toString())
                    putString("DOB" + data + currentuser,dob.text.toString())
                    putString("Regno" + data + currentuser,regno.text.toString())
                    apply()
                }
                Intent(this, StudentsActivity :: class.java).also{
                    startActivity(it)
                    finish()
                }
            }
            else{
                Toast.makeText(this, "One or more field are empty",Toast.LENGTH_SHORT).show()
            }

        }
    }
    fun isDetailsValid(name : String, age : String, dept : String, year : String, dob : String, regno : String) : Boolean{
        if(name.length > 0 &&
            age.length > 0 &&
            dept.length > 0 &&
            year.length > 0 &&
            dob.length > 0 &&
            regno.length > 0)
        {
            return true
        }
        return false
    }
}