package com.dr.predulive.navigationView.common

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dr.predulive.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_password.*


class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)


    }

    fun resetPassword(view: View) {

        if(TextUtils.isEmpty(resetPasswordEmail.text.toString().trim())) {
            resetPasswordEmail.error = "Email Required"
        } else {

            FirebaseAuth.getInstance().sendPasswordResetEmail(resetPasswordEmail.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email Sent!", Toast.LENGTH_SHORT).show()
                    }
                    if (!task.isSuccessful) {
                        Toast.makeText(this, "Please enter correct Email!", Toast.LENGTH_LONG)
                            .show()
                    }
                }
        }
    }

    fun back(view: View) {
        finish()
    }

}