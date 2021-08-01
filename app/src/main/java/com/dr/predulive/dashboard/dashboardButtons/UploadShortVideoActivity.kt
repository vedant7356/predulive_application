package com.dr.predulive.dashboard.dashboardButtons

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dr.predulive.R
import com.dr.predulive.daos.ShortVideosDao
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_upload_short_video.*

class UploadShortVideoActivity : AppCompatActivity() {

    var videoView: VideoView? = null
    var choose_btn: Button? = null
    var upload_btn:android.widget.Button? = null
    var videoUrl: Uri? = null
    var mediaController: MediaController? = null
    val REQUEZT_Video_Code = 101
    var storageReference: StorageReference? = null
    var et_title: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_short_video)

        init()

        videoView!!.setMediaController(mediaController)
        videoView!!.start()

        choose_btn!!.setOnClickListener {
            Dexter.withContext(applicationContext)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                        Log.i("permission", "Granted")
                        val iotent = Intent()
                        iotent.type = "video/*"
                        iotent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(iotent, REQUEZT_Video_Code)
                    }

                    override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                        Log.i("permission", "Denied")
                        val intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivityForResult(intent, REQUEZT_Video_Code)
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissionRequest: PermissionRequest,
                        permissionToken: PermissionToken
                    ) {
                        permissionToken.continuePermissionRequest()
                    }
                }).check()
        }
        upload_btn!!.setOnClickListener {
            if (videoUrl != null) {
                Log.i("permission", "processUploadVideo")
                PROCESSVIDEOUPLOAD()
            } else {
                Toast.makeText(this, "please select data.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun init() {

        videoView = findViewById<View>(R.id.ins_video) as VideoView
        choose_btn = findViewById<View>(R.id.btn_choose_vedio) as Button
        upload_btn = findViewById<View>(R.id.btn_upload_vedio) as Button
        et_title = findViewById<View>(R.id.et_inspire_title) as EditText
        mediaController = MediaController(this)
        //storage type castig
        storageReference = FirebaseStorage.getInstance().reference

    }

    fun Extension(): String? {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(videoUrl!!))
    }

    private fun PROCESSVIDEOUPLOAD() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading Video")
        progressDialog.show()

        val shortVideosReference =
            storageReference!!.child("shortVideos/" + System.currentTimeMillis() + "." + Extension())

        Log.i("permission", shortVideosReference.toString())
        Log.i("permission", shortVideosReference.downloadUrl.toString())

        shortVideosReference.putFile(videoUrl!!)
            .addOnSuccessListener {
                shortVideosReference.downloadUrl.addOnSuccessListener { uri ->

                    // adding shortVideo data to database
                    val shortVideoDao = ShortVideosDao()

                    shortVideoDao.addShortVideo(
                        et_title?.text.toString().trim(),
                        uri.toString(),
                        uploadVideoDescription.text.toString().trim()
                    )
                    progressDialog.dismiss()
                    finish()

                }
            }
            .addOnProgressListener { snapshot ->
                val per = (100 * snapshot.bytesTransferred / snapshot.totalByteCount).toFloat()
                progressDialog.setMessage("uploaded : " + per.toInt() + "%")
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEZT_Video_Code && resultCode == RESULT_OK) {
            videoUrl = data!!.data
            videoView!!.setVideoURI(videoUrl)
        }
    }


}