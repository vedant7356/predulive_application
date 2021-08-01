package com.dr.predulive.dashboard.uploadedCourses

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dr.predulive.R

class Description : AppCompatActivity() {

    var mProgressBar: ProgressBar? = null
    var mCountDownTimer: CountDownTimer? = null
    var i = 0

    lateinit var tv1: TextView
    lateinit var tv2:TextView
    lateinit var tv3:TextView
    lateinit var b1: Button
    lateinit var v1: VideoView
    lateinit var s1: String
    lateinit var s2:kotlin.String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        tv1 = findViewById<View>(R.id.course_title) as TextView
        tv2 = findViewById<View>(R.id.desciption) as TextView
        tv3 = findViewById<View>(R.id.creator) as TextView
        s1 = intent.getStringExtra("video_url").toString()
        s2 = intent.getStringExtra("pdf_url").toString()
        b1 = findViewById<View>(R.id.view_pdf_control) as Button
        v1 = findViewById<View>(R.id.video_id_view) as VideoView


        tv1.text = intent.getStringExtra("name")
        tv3.text = intent.getStringExtra("course")
        tv2.text = intent.getStringExtra("email")

        v1.setVideoURI(Uri.parse(s1))
        v1.setMediaController(MediaController(this))
        v1.requestFocus()

        mProgressBar = findViewById<View>(R.id.progressbar) as ProgressBar
        mProgressBar!!.progress = i
        mCountDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.v("Log_tag", "Tick of Progress$i$millisUntilFinished")
                i++
                mProgressBar!!.progress = i as Int * 50 / (5000 / 1000)
            }

            override fun onFinish() {
                v1.start()
                Toast.makeText(this@Description, "Playing VIDEO.....", Toast.LENGTH_SHORT).show()
                i++
                mProgressBar!!.progress = 100
                mProgressBar!!.visibility = View.GONE
            }
        }
        (mCountDownTimer as CountDownTimer).start()

        b1!!.setOnClickListener {
            val intent = Intent(this@Description, View_Pdf::class.java)
            intent.putExtra("PDF_URL_FINAL", s2)
            startActivity(intent)
        }


    }
}