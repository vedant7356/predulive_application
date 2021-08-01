package com.dr.predulive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.dr.predulive.authentication.LoginActivity
import com.dr.predulive.authentication.SignUpActivity
import com.dr.predulive.dashboard.DashboardActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler = Handler()
        handler.postDelayed(Runnable {
            login()
            finish()
        }, 1000)


    }

    fun login() {

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }
    fun signup(view: View) {

        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun mainDashboard(view: View) {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }




}