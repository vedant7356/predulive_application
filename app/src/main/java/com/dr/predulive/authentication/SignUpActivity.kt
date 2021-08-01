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
import com.dr.predulive.daos.UserDao
import com.dr.predulive.dashboard.DashboardActivity
import com.dr.predulive.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var signUpSpinner: Spinner
    private val TAG = "SignUpActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // full screen activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_sign_up)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // adding spinner dropdown list
        signUpSpinner = findViewById<View>(R.id.signUpSpinnerView) as Spinner

        var mySpinnerAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.userType)
        )
        mySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        signUpSpinner.adapter = mySpinnerAdapter

    }

    fun onLoginClick(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    fun registerUserButton(view: View) {
        if (TextUtils.isEmpty(signUpNameEditTextView.text.trim())) {
            signUpNameEditTextView.error = "Name is required"
        } else if (TextUtils.isEmpty(signUpEmailEditText.text.trim())) {
            signUpEmailEditText.error = "Password is required"
        } else if (TextUtils.isEmpty(signUpPasswordEditText.text.trim())) {
            signUpPasswordEditText.error = "Password is required"
        } else if (TextUtils.isEmpty(signUpConfirmPasswordEditText.text.trim())) {
            signUpConfirmPasswordEditText.error = "Password is required"
        } else if (signUpPasswordEditText.text.toString().trim() != signUpConfirmPasswordEditText.text.toString().trim()) {
            Toast.makeText(this, "Password must confirm", Toast.LENGTH_SHORT).show()
        } else if (signUpSpinner.selectedItem.toString() == "Select") {
            Toast.makeText(this, "Select account Type", Toast.LENGTH_SHORT).show()
        } else {
            signUp()
        }
    }

    private fun signUp() {

        var email = signUpEmailEditText.text.toString().trim()
        var pass = signUpPasswordEditText.text.toString().trim()

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
//                    val user = auth.currentUser

                    updateUI()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun googleSignUp(view: View) {}

    private fun updateUI() {

        var user = User(
            auth.uid.toString(),
            signUpNameEditTextView.text.toString().trim(),
            signUpEmailEditText.text.toString().trim(),
            "https://cdn0.iconfinder.com/data/icons/linkedin-ui-colored/48/JD-07-512.png",
            signUpSpinner.selectedItem.toString().trim(),
            "Add details",
            "Add Institute Name",
            "https://miro.medium.com/max/800/1*hFwwQAW45673VGKrMPE2qQ.png"
        )
        val userDao = UserDao()
        userDao.addUser(user)

        var intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }
}