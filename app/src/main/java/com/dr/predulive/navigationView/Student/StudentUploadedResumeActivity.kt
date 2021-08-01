package com.dr.predulive.navigationView.Student

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.dr.predulive.R
import com.dr.predulive.daos.UserDao
import com.dr.predulive.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import es.voghdev.pdfviewpager.library.RemotePDFViewPager
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter
import es.voghdev.pdfviewpager.library.remote.DownloadFile
import kotlinx.android.synthetic.main.activity_student_uploaded_resume.*


class StudentUploadedResumeActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var uid: String
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_uploaded_resume)

        mAuth = FirebaseAuth.getInstance()
        auth = Firebase.auth
        uid = mAuth.uid.toString()
        storage = Firebase.storage

        pdfView.fromUri(url.toString().toUri()).load()

        val userDao = UserDao()
        userDao.getUserById(auth.uid.toString()).addOnSuccessListener {
            var user: User = it.toObject<User>()!!
            Log.i("resume url is ", user.resume)
        }

    }


    fun upload(view: View?) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            getPhoto()
        }
    }
    private fun getPhoto() {
        val galleryIntent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT

        // We will be redirected to choose pdf

        // We will be redirected to choose pdf
        galleryIntent.type = "application/pdf"
        startActivityForResult(galleryIntent, 1)
    }
    var dialog: ProgressDialog? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val selectedImage = data!!.data
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            try {
                next(data)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun next(data: Intent) {
        dialog = ProgressDialog(this);
        dialog!!.setMessage("Uploading");
        dialog!!.show();
        val myResume: Uri? = data.data;
        val fileName = FirebaseAuth.getInstance().uid.toString() + ".pdf"

        val storage = Firebase.storage

        val storageRef = storage.reference.child("resume").child(fileName)
        val uploadTask = myResume?.let { storageRef.putFile(it) }

        uploadTask?.addOnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(this, "upload UN-Successful", Toast.LENGTH_SHORT).show()
            dialog!!.dismiss()
        }?.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            Toast.makeText(this, "upload Successful", Toast.LENGTH_SHORT).show()

            // getting download URL
            if (it.metadata != null) {
                if (it.metadata!!.reference != null) {
                    val result = it.storage.downloadUrl
                    result.addOnSuccessListener { uri ->
                        val resumeUrl = uri.toString()
                        val userDao = UserDao()
                        userDao.setUserProperty(uid, "resume", resumeUrl)
                    }
                }
            }

            dialog!!.dismiss()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto()
            }
        }
    }


    var url = "http://www.cals.uidaho.edu/edComm/curricula/CustRel_curriculum/content/sample.pdf"



}