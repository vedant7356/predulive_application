package com.dr.predulive.dashboard.bottomNavigationViewFragments

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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


class uploadFragment : Fragment() {

//    lateinit var videoView: VideoView
    lateinit var choose_btn: Button
    lateinit var upload_btn: Button
    lateinit var videoUrl: Uri
    lateinit var mediaController: MediaController
    val REQUEZT_Video_Code = 101
    lateinit var storageReference: StorageReference
    lateinit var et_title: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

//        videoView = view.findViewById<View>(R.id.ins_video2) as VideoView
        choose_btn = view.findViewById<View>(R.id.btn_choose_vedio1) as Button
        upload_btn = view.findViewById<View>(R.id.btn_upload_vedio1) as Button
        et_title = view.findViewById<View>(R.id.et_inspire_title1) as EditText
        mediaController = MediaController(context)

        //storage type castig
        storageReference = FirebaseStorage.getInstance().reference
//        videoView.setMediaController(mediaController)
//        videoView.start()

        choose_btn.setOnClickListener {
            Dexter.withContext(context)
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
                        val uri = Uri.fromParts("package", context!!.packageName, null)
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
            Log.i("permission", "processUploadVideo")
            PROCESSVIDEOUPLOAD()
        }


        return view
    }


    fun Extension(): String? {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(requireContext().contentResolver.getType(
            videoUrl
        ))
    }

    private fun PROCESSVIDEOUPLOAD() {
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Uploading Video")
        progressDialog.show()

        val shortVideosReference =
            storageReference.child("shortVideos/" + System.currentTimeMillis() + "." + Extension())

        Log.i("permission", shortVideosReference.toString())
        Log.i("permission", shortVideosReference.downloadUrl.toString())

        shortVideosReference.putFile(videoUrl)
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
//                    finish()

                }
            }
            .addOnProgressListener { snapshot ->
                val per = (100 * snapshot.bytesTransferred / snapshot.totalByteCount).toFloat()
                progressDialog.setMessage("uploaded : " + per.toInt() + "%")
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEZT_Video_Code && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                videoUrl = data.data!!
            }
//            videoView.setVideoURI(videoUrl)
        }
    }

}