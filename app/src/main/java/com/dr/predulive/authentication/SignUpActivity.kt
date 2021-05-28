package com.dr.predulive.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dr.predulive.R

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    fun onLoginClick(view: View) {}
    fun registerUserButton(view: View) {}
    fun googleSignUp(view: View) {}
}