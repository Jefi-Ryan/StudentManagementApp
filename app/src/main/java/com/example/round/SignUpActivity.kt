package com.example.round

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.round.databinding.ActivitySignUpBinding
import com.google.android.material.appbar.AppBarLayout

class SignUpActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        // Sign up
        val signup = findViewById<Button>(R.id.btnSignupreal)
        var name = findViewById<EditText>(R.id.etNameSignup)
        val passwd = findViewById<EditText>(R.id.etPasswordSignup)
        lateinit var email: String
        lateinit var username: String
        var phoneno: Int?
        val sharedpref = getSharedPreferences("Users", MODE_PRIVATE)
        val cachepref = getSharedPreferences("Cache", MODE_PRIVATE)
        supportActionBar?.show()
        supportActionBar?.setDisplayShowTitleEnabled(true)

        signup.setOnClickListener {
            if (name == null) {
                username = ""
            } else {
                username = name.text.toString()
            }
            if (isValidEmail(username) || isValidPhoneNo(username)) {
                if(isValidPasswd(passwd.text.toString())){
                    val checkusr = sharedpref.getString(username,"")
                    Log.d("SignupActivity", "Past user : $checkusr")
                    if(checkusr?.equals(passwd.text.toString()) == true){
                        Toast.makeText(this, "Username already exist!",Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    val editor = sharedpref.edit()
                    editor.apply {
                        putString(name.text.toString(), passwd.text.toString())
                        apply()
                    }
                    val cacheeditor = cachepref.edit()
                    cacheeditor.apply {
                        putString("Username", name.text.toString())
                        apply()
                    }
                    Intent(this, OtpActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }
                else {
                    Toast.makeText(this, "Password length must be 6 or higher", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Invalid phone no or email id", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun isValidPasswd(data : String) : Boolean{
        if(data.length < 6){
            return false
        }
        return true
    }
}

fun isValidEmail(target: CharSequence?): Boolean {
    return if (TextUtils.isEmpty(target)) {
        false
    } else {
        if (target != null) {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
        return false
    }
}

fun isValidPhoneNo(target: CharSequence?): Boolean {
    return if (TextUtils.isEmpty(target)) {
        false
    } else {
        if (target != null) {
            return Patterns.PHONE.matcher(target).matches()
        }
        return false
    }
}