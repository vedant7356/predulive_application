package com.dr.predulive

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.dr.predulive.authentication.SignUpActivity
import com.dr.predulive.dashboard.DashboardActivity

class MainActivity : AppCompatActivity() {

    lateinit var head_text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        head_text = findViewById<View>(R.id.carRent) as TextView

        YoYo.with(Techniques.BounceInUp).duration(1000).repeat(3).playOn(head_text)

        val handler = Handler()
        handler.postDelayed(Runnable {
            login()
            finish()
        }, 3000)


    }

    fun login() {
        val intent = Intent(this, Intro_Activity::class.java)
        startActivity(intent)
        Animatoo.animateZoom(this)

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