package com.dr.predulive.dashboard.uploadedCourses

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dr.predulive.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class Create_new_courses : AppCompatActivity() {

    var videoView: VideoView? = null
    var browse: Button? = null
    var upload:android.widget.Button? = null
    var pdf_upload:android.widget.Button? = null
    var videouri: Uri? = null
    var filepath:android.net.Uri? = null
    var mediaController: MediaController? = null
    var storageReference: StorageReference? = null
    var et1: EditText? = null
    var et2:EditText? = null
    var et3:EditText? = null
    var databaseReference: DatabaseReference? = null
    var pdf: String? = null
    var image:kotlin.String? = null
    var pdf_data:kotlin.String? = null
    var flag1 = 0
    var flag2:Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_courses)


        storageReference = FirebaseStorage.getInstance().reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("students")

        image =
            "https://firebasestorage.googleapis.com/v0/b/viewcoursesdata.appspot.com/o/Pictures%2Fprofile.jpg?alt=media&token=bff270bf-a7b9-4ab1-bea2-07625ddbb427"
        et1 = findViewById<View>(R.id.Enter_title_here) as EditText
        et2 = findViewById<View>(R.id.Enter_username_here) as EditText
        et3 = findViewById<View>(R.id.Enter_desc_here) as EditText
        videoView = findViewById<View>(R.id.videoView) as VideoView
        pdf_upload = findViewById<View>(R.id.upload_pdf) as Button
        upload = findViewById<View>(R.id.upload) as Button
        browse = findViewById<View>(R.id.browse) as Button

        mediaController = MediaController(this)
        videoView!!.setMediaController(mediaController)
        videoView!!.start()

        if (flag1 == 0 && flag2 == 0) {
            upload!!.setEnabled(false)
        } else if (flag1 == 1 && flag2 == 2) {
            upload!!.setEnabled(true)
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
        browse!!.setOnClickListener {
            Dexter.withContext(applicationContext)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                        val intent = Intent()
                        intent.type = "video/*"
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(intent, 101)
                    }

                    override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {}
                    override fun onPermissionRationaleShouldBeShown(
                        permissionRequest: PermissionRequest,
                        permissionToken: PermissionToken
                    ) {
                        permissionToken.continuePermissionRequest()
                    }
                }).check()
        }

        pdf_upload!!.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.type = "application/pdf"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Pdf Files"), 1)
            upload!!.setEnabled(true)

            //startActivity(new Intent(Create_new_courses.this, PDF_UPLOAD.class));
        })


        upload!!.setOnClickListener(View.OnClickListener {
            val title: String
            val bywhom: String
            val desc: String
            title = et1!!.text.toString()
            bywhom = et2!!.getText().toString()
            desc = et3!!.getText().toString()
            if (TextUtils.isEmpty(title)) {
                et1!!.error = "Email is Required."
                return@OnClickListener
            }
            if (TextUtils.isEmpty(bywhom)) {
                et2!!.setError("Password is Required.")
                return@OnClickListener
            }
            if (TextUtils.isEmpty(desc)) {
                et3!!.setError("Password is Required.")
                return@OnClickListener
            }
            processvideouploading()
        })


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            101 -> {
                videouri = data!!.data
                videoView!!.setVideoURI(videouri)
                Toast.makeText(this, "Video Updated", Toast.LENGTH_SHORT).show()
                flag1 = 1
            }
            1 -> {
                filepath = data!!.data
                Toast.makeText(this, "PDF Updated", Toast.LENGTH_SHORT).show()
                flag2 = 2
            }
            else -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun getExtension(): String? {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(videouri!!))
    }

    fun processvideouploading() {
        val pd = ProgressDialog(this)
        pd.setTitle("Media Uploader")
        pd.show()
        val reference = storageReference!!.child("pdf_files/" + System.currentTimeMillis() + ".pdf")
        filepath?.let {
            reference.putFile(it).addOnSuccessListener {
                reference.downloadUrl.addOnSuccessListener { uri ->
                    pdf_data = uri.toString()
                }
            }
        }
        val uploader =
            storageReference!!.child("videos/" + System.currentTimeMillis() + "." + getExtension())
        uploader.putFile(videouri!!)
            .addOnSuccessListener {
                uploader.downloadUrl.addOnSuccessListener { uri ->
                    val model = model(
                        et2!!.getText().toString(),
                        et3!!.getText().toString(),
                        et1!!.text.toString(),
                        image,
                        uri.toString(),
                        pdf_data
                    )
                    databaseReference!!.child(databaseReference!!.push().key!!).setValue(model)
                        .addOnSuccessListener {
                            pd.dismiss()
                            Toast.makeText(
                                applicationContext,
                                "Successfully uploaded",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(Intent(this@Create_new_courses, UploadedCoursesActivity::class.java))
                        }
                        .addOnFailureListener {
                            pd.dismiss()
                            Toast.makeText(
                                applicationContext,
                                "Failed to upload",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
            }.addOnProgressListener { snapshot ->
                val per = (100 * snapshot.bytesTransferred / snapshot.totalByteCount).toFloat()
                pd.setMessage("Uploaded :" + per.toInt() + "%")
            }.addOnFailureListener { e ->
                Log.i("failure", "-----------------------------------------------")
                Log.i("exception", e.message!!)
                e.printStackTrace()
            }
    }


}