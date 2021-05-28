package com.dr.predulive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dr.predulive.authentication.LoginActivity
import com.dr.predulive.authentication.SignUpActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun login(view: View) {

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }
    fun signup(view: View) {

        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}