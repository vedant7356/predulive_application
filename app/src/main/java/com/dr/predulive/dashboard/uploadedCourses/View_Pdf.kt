package com.dr.predulive.dashboard.uploadedCourses

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.dr.predulive.R
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class View_Pdf : AppCompatActivity() {

    var t1: TextView? = null
    var pdfView: PDFView? = null
    var firebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view__pdf)


        // String fileurl=getIntent().getStringExtra("pdf_url");
        pdfView = findViewById<View>(R.id.pdf_viewer) as PDFView
        t1 = findViewById<View>(R.id.text1) as TextView

        t1!!.text = intent.getStringExtra("PDF_URL_FINAL")
        val url = t1!!.text.toString()

        // PLS IGNORE url="https://firebasestorage.googleapis.com/v0/b/viewcoursesdata.appspot.com/o/uploads%2F1624632351770.pdf?alt=media&token=b3c53581-d970-4059-b213-694d6fa77984";

        //  PLS IGNORE Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        // PLS IGNORE url="https://firebasestorage.googleapis.com/v0/b/viewcoursesdata.appspot.com/o/uploads%2F1624632351770.pdf?alt=media&token=b3c53581-d970-4059-b213-694d6fa77984";

        //  PLS IGNORE Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
//        var e: InputStream?
//        GlobalScope.launch(Dispatchers.IO) {
//            e = RetrivePdfStream().getStream(url)
//        }

//        pdfView.fromStream(e).load()

//        pdfView!!.

    }

    class RetrivePdfStream {

        fun getStream(uurl: String): InputStream? {
            var inputStream: InputStream? = null
            try {
                val url = URL(uurl)
                val urlConnection = url.openConnection() as HttpURLConnection
                if (urlConnection.responseCode == 200) {
                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
            } catch (e: IOException) {
                return null
            }
            return inputStream
        }
    }
}