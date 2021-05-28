package com.dr.predulive.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dr.predulive.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onLoginClick(view: View) {}
    fun googleSignIn(view: View) {}
    fun registerNewUser(view: View) {}
    fun forgotUserPassword(view: View) {}
}