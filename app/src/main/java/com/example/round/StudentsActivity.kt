package com.example.round

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration


class StudentsActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students)

        val sharedpref = getSharedPreferences("Students", MODE_PRIVATE)
        val searchbtn = findViewById<Button>(R.id.btnSearch)
        val tvname = findViewById<TextView>(R.id.tvName)
        val tvage = findViewById<TextView>(R.id.tvAge)
        val tvdept = findViewById<TextView>(R.id.tvDept)
        val tvyear = findViewById<TextView>(R.id.tvYear)
        val tvdob = findViewById<TextView>(R.id.tvDOB)
        val tvregno = findViewById<TextView>(R.id.tvRegno)
        val search = findViewById<EditText>(R.id.etSearch)

        val cache = getSharedPreferences("Cache", MODE_PRIVATE)
        val currentuser = cache.getString("Username", "").toString().lowercase()
        searchbtn.setOnClickListener {
            val data = search.text.toString().lowercase()
            val name = sharedpref.getString("Name" + data + currentuser, "")
            val age = sharedpref.getString("Age" + data + currentuser, "")
            val dept = sharedpref.getString("Dept" + data + currentuser, "")
            val year = sharedpref.getString("Year" + data + currentuser, "")
            val dob = sharedpref.getString("DOB" + data + currentuser, "")
            val regno = sharedpref.getString("Regno" + data + currentuser, "")
            if(name?.length == 0){
                Toast.makeText(this,"Register number not found",Toast.LENGTH_SHORT).show()
            }
            else {
                Log.d("StudentDetails", "Name : $name, Age : $age, " +
                        "Dept : $dept, Year : $year, DOB : $dob, " +
                        "Regno : $regno")
                tvname.setText(name)
                tvage.setText(age)
                tvdept.setText(dept)
                tvyear.setText(year)
                tvdob.setText(dob)
                tvregno.setText(regno)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbarmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.title){
            "Add Student" -> {
                Intent(this, AskStudentDetails :: class.java).also {
                    startActivity(it)
                }
            }
            "Logout" -> {
                Intent(this, MainActivity::class.java).also {
                    val cache = getSharedPreferences("Cache", MODE_PRIVATE)
                    val editor = cache.edit()
                    editor.clear()
                    Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                    startActivity(it)
                    finish()
                }
            }
        }
        return true
    }
}
