package com.example.round

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = mutableListOf<String>()
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            data.add(android.Manifest.permission.SEND_SMS)
        }
        if(data.isNotEmpty()){
            ActivityCompat.requestPermissions(this, data.toTypedArray(),0)
        }

        val login = findViewById<Button>(R.id.btnLogin)
        val name = findViewById<EditText>(R.id.etName)
        val passwd = findViewById<EditText>(R.id.etPassword)
        // Login

        val sharedpref = getSharedPreferences("Users", MODE_PRIVATE)

        val signup = findViewById<Button>(R.id.btnSignup)
        signup.setOnClickListener {
            Intent(this, SignUpActivity :: class.java).also {
                startActivity(it)
            }
        }

        val cachepref = getSharedPreferences("Cache", MODE_PRIVATE)
        login.setOnClickListener {
            val editor = cachepref.edit()
            editor.apply {
                putString("Username", name.text.toString())
                putInt("RecentOtp",0)
                apply()
            }
            val result = sharedpref.getString(name.text.toString(),"")
            val verified = sharedpref.getBoolean(name.text.toString() + "OTP",false)
            Log.d("OTPActivity", "${name.text.toString()} : ${if(verified) "verified" else "Not verified"}")
            if(result.equals(passwd.text.toString()) && result?.length != 0 && verified){
                Log.d("MainActivity", "Login : True")
                Intent(this, StudentsActivity :: class.java).also {
                    startActivity(it)
                }
            }
            else if(result.equals(passwd.text.toString()) && result?.length != 0 && !verified){
                Intent(this, OtpActivity :: class.java).also {
                    startActivity(it)
                }
            }
            else{
                Log.d("MainActivity", "Login : False")
                Toast.makeText(this, "Invalid Username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.isNotEmpty() && requestCode == 0){
            for(i in grantResults.indices){
                if(grantResults[i] == 1){
                    Log.d("Permission", "${permissions[i]} granted")
                }
            }
        }
    }


}