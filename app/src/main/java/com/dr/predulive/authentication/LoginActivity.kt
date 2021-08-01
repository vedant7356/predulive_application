package com.dr.predulive.authentication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dr.predulive.R
import com.dr.predulive.dashboard.DashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // full screen activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // adding spinner dropdown list
        var signInSpinner = findViewById<View>(R.id.signInSpinner) as Spinner

        var mySpinnerAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.userType)
        )
        mySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        signInSpinner.adapter = mySpinnerAdapter



    }

    fun onLoginClick(view: View) {

        if (TextUtils.isEmpty(loginEmailEditText.text.trim())) {
            loginEmailEditText.error = "Name is required"
        } else if (TextUtils.isEmpty(loginPasswordEditText.text.trim())) {
            loginPasswordEditText.error = "Password is required"
        } else if (signInSpinner.selectedItem.toString() == "Select" || signInSpinner.selectedItem.toString() == "") {
            Toast.makeText(this, "Select account Type", Toast.LENGTH_SHORT).show()
        } else {
            signIn()
        }
    }
    fun googleSignIn(view: View) {}

    fun registerNewUser(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
    fun forgotUserPassword(view: View) {
        Toast.makeText(this, "what, You Forgot password again !", Toast.LENGTH_SHORT).show()
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI();
        }
    }

    private fun signIn() {
        auth.signInWithEmailAndPassword(loginEmailEditText.text.toString().trim(), loginPasswordEditText.text.toString().trim())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI() {
        var intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }
}