package com.dr.predulive.navigationView.common

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dr.predulive.R
import com.dr.predulive.daos.UserDao
import com.dr.predulive.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.ByteArrayOutputStream

class EditProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var imageName: String
    private lateinit var uid: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var imageUrl: String
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        auth = Firebase.auth
        uid = mAuth.uid.toString()
        imageName = "$uid.jpg"
        storage = Firebase.storage

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("loading...")
        progressDialog.show()

        editProfileUpdateButton.visibility = View.INVISIBLE

        val editProfileUserType = findViewById<View>(R.id.editProfileUserType) as TextView


        val userDao = UserDao()
        userDao.getUserById(auth.uid.toString()).addOnSuccessListener {
            var user: User = it.toObject<User>()!!

            editProfileName.setText(user.displayName)
            editProfileEmail.setText(user.email)
            editProfileDetails.setText(user.details)
            editProfileInstituteName.setText(user.instituteName)
            imageUrl = user.imageUrl
            Glide.with(this)
                .load(user.imageUrl)
                .placeholder(R.drawable.loading_layout)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(editProfileImage)
            editProfileUserType.text = user.userType

            editProfileUpdateButton.visibility = View.VISIBLE
            progressDialog.dismiss()
        }
    }

    fun updateUserProfile(view: View) {
        Toast.makeText(this, "updateUserProfile clicked", Toast.LENGTH_SHORT).show()

        var user = User(
            uid,
            editProfileName.text.toString().trim(),
            editProfileEmail.text.toString().trim(),
            imageUrl.toString(),
            editProfileUserType.toString().trim(),
            editProfileDetails.text.toString().trim(),
            editProfileInstituteName.text.toString().trim()
        )
        val userDao = UserDao()
        userDao.updateUser(user)
        Toast.makeText(this, "Successfully updated", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun chooseImage(view: View?) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            getPhoto()
        }
    }
    fun getPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val selectedImage = data!!.data
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            try {
                val bitmap = ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        this.contentResolver,
                        selectedImage!!
                    )
                )
                editProfileImage.setImageBitmap(bitmap)
                next(bitmap)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun next(bitmap: Bitmap) {
        editProfileUpdateButton.visibility = View.INVISIBLE
        progressDialog.show()
        // Get the data from an ImageView as bytes
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        val data = baos.toByteArray()

        // Create a storage reference from our app
        val storageRef = storage.reference.child("images").child(imageName)
        val uploadTask = storageRef.putBytes(data)

        uploadTask.addOnFailureListener { // Handle unsuccessful uploads
            Toast.makeText(applicationContext, "Upload Unsuccessful", Toast.LENGTH_SHORT).show()

            progressDialog.dismiss()
            editProfileUpdateButton.visibility = View.VISIBLE

        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
            Toast.makeText(applicationContext, "Successful", Toast.LENGTH_SHORT).show()

            // getting download URL
            if (taskSnapshot.metadata != null) {
                if (taskSnapshot.metadata!!.reference != null) {
                    val result = taskSnapshot.storage.downloadUrl
                    result.addOnSuccessListener { uri ->
                        imageUrl = uri.toString()

                        progressDialog.dismiss()
                        editProfileUpdateButton.visibility = View.VISIBLE

                    }
                }
            }
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

    fun profileBackButton(view: View) {
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}