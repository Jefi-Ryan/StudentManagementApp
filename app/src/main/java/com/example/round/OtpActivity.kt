package com.example.round

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.ui.AppBarConfiguration
import com.example.round.databinding.ActivityOtpBinding


class OtpActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    val CHANNEL_ID = "1"
    val CHANNEL_NAME = "Student notifier"
    val NOTIFICATION_ID = 0
    var recentOtp = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        val etotp = findViewById<EditText>(R.id.etOtp)
        val otps = listOf(23234, 45623, 25345, 45657, 76789, 45653, 23435, 34546, 56764, 87235)
        val tvstatus = findViewById<TextView>(R.id.tvOtpStatus)
        val verifybtn = findViewById<ImageButton>(R.id.ibtnVerifyOtp)
        val generatebtn = findViewById<Button>(R.id.btnGenerateOtp)
        supportActionBar?.show()
        supportActionBar?.setDisplayShowTitleEnabled(true)

        // Notification
        createNotificationChannel()
        //

        val cachepref = getSharedPreferences("Cache", MODE_PRIVATE)
        val sharedpref = getSharedPreferences("Users", MODE_PRIVATE)
        val usereditor = sharedpref.edit()

        val username = cachepref.getString("Username","")
        val editor = cachepref.edit()

        lateinit var email : String
        var phone : Int?


        verifybtn.setOnClickListener {
            if(etotp.text.toString().toInt() in otps){
                usereditor.apply {
                    putBoolean(username + "OTP",true)
                    apply()
                }
                Log.d("OTPActivity", "$username : verified")
                Intent(this, AskStudentDetails :: class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }

        generatebtn.setOnClickListener {

            recentOtp = cachepref.getInt("RecentOtp",0)
            if(recentOtp == otps.size){
                recentOtp = 0
            }
            val otp = otps.get(recentOtp)
            recentOtp++
            if(recentOtp == otps.size){
                recentOtp = 0
            }
            editor.apply {
                putInt("RecentOtp",recentOtp)
                apply()
            }
            val msg = "You OTP is $otp"
            if (username != null) {
                sendOtp(username, msg)
                tvstatus.setText("OTP is sent!")
            }
        }
    }

    fun sendOtp(username : String, msg : String){
        if(isValidEmail(username)){
            val i = Intent(Intent.ACTION_SEND)
            i.type = "message/rfc822"
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf(username))
            i.putExtra(Intent.EXTRA_SUBJECT, "OTP")
            i.putExtra(Intent.EXTRA_TEXT, msg)
            startActivity(Intent.createChooser(i, "Send mail..."))
        }
        else if(isValidPhoneNo(username)){

            val intentofMain = Intent(this, OtpActivity::class.java)
            val pendingIntent = TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(intentofMain)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }

            val notification = NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("OTP")
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_alert_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            val notification_manager = NotificationManagerCompat.from(this)

            var pending_permissions = mutableListOf<String>()
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                pending_permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }
            if(pending_permissions.size > 0){
                ActivityCompat.requestPermissions(this,pending_permissions.toTypedArray(),0)
            }
            notification_manager.notify(NOTIFICATION_ID,notification)
        }
    }

    fun createNotificationChannel(){
        val notification_channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        val notification_manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notification_manager.createNotificationChannel(notification_channel)
    }



}