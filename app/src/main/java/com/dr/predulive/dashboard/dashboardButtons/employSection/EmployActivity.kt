package com.dr.predulive.dashboard.dashboardButtons.employSection

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.dr.predulive.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class EmployActivity : AppCompatActivity() {

    var add_resume:android.widget.Button? = null
    var upload_resume:android.widget.Button? = null
    var emp_contact:android.widget.Button? = null
    var emp_details:android.widget.Button? = null
    var up_eve_add:android.widget.Button? = null
    var filepath: Uri? = null
    var storageReference: StorageReference? = null
    var userid: String? = null
    var firestore: FirebaseFirestore? = null
    var fAuth: FirebaseAuth? = null
    var blink_the_img:android.widget.ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employ)
        blink_the_img=findViewById<View>(R.id.blink_image) as ImageView

        YoYo.with(Techniques.Flash).duration(5000).repeat(15).playOn(blink_the_img)
        fAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        firestore = FirebaseFirestore.getInstance()

        add_resume = findViewById<View>(R.id.add_res) as Button
        upload_resume = findViewById<View>(R.id.add_res2) as Button
        emp_details = findViewById<View>(R.id.up_eve_details) as Button
        emp_contact = findViewById<View>(R.id.emp_contact_data) as Button
        up_eve_add = findViewById<View>(R.id.up_event) as Button
        upload_resume!!.isEnabled = false
        up_eve_add!!.isEnabled = false

        emp_details!!.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    UpcomingEventsActivity::class.java
                )
            )
        }

        up_eve_add!!.setOnClickListener {
            //startActivity(new Intent(Main_Page.this, Up_eve.class));
        }

        emp_contact!!.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Employ_Employer_List_Activity::class.java
                )
            )
        }

        add_resume!!.setOnClickListener { upload_pdf() }

        upload_resume!!.setOnClickListener { start_process() }

    }

    private fun start_process() {
        val fileName = FirebaseAuth.getInstance().uid.toString() + ".pdf"
        val reference = storageReference!!.child("resume").child(fileName)
        reference.putFile(filepath!!).addOnSuccessListener {
            reference.downloadUrl.addOnSuccessListener { uri ->
                val documentReference =
                    FirebaseFirestore.getInstance().collection("users").document(
                        fAuth!!.uid!!
                    )
                val user: MutableMap<String, Any> =
                    HashMap()
                user.put("resume", uri.toString())
                documentReference.update(user).addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "Resume Updated",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Resume Updatation Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun upload_pdf() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Pdf Files"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            filepath = data!!.data
            Toast.makeText(this, "PDF Updated", Toast.LENGTH_SHORT).show()
            upload_resume!!.isEnabled = true
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            upload_resume!!.isEnabled = false
        }
    }
}