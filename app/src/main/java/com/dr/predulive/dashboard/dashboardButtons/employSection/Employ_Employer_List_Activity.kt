package com.dr.predulive.dashboard.dashboardButtons.employSection

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dr.predulive.R
import com.dr.predulive.models.emplist
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*

class Employ_Employer_List_Activity : AppCompatActivity() {


    var arrayList: ArrayList<emplist>? = null
    var recview: RecyclerView? = null
    var adapter: EmployerListAdapter? = null
    var db: FirebaseFirestore? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employ__employer__list_)

        progressDialog = ProgressDialog(this)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Fetching Data")
        progressDialog!!.show()
        recview = findViewById<View>(R.id.employerListRecyclerView) as RecyclerView
        recview!!.setHasFixedSize(true)
        recview!!.layoutManager = LinearLayoutManager(this)

        db = FirebaseFirestore.getInstance()
        arrayList = ArrayList<emplist>()

        adapter = EmployerListAdapter(this, arrayList!!)

        recview!!.adapter = adapter

        EventChangeListener()

        Toast.makeText(this, "Fetching ONLY COMPANY DATA", Toast.LENGTH_SHORT).show()


        // processsearch();


    }

    private fun processsearch() {
        val list: ArrayList<emplist> = ArrayList<emplist>()
        for (ob in arrayList!!) {
            if (ob.getUserType().equals("Company")) {
                list.add(ob)
            }
        }
        val myadapter = EmployerListAdapter(this, list)
        recview!!.adapter = myadapter
    }

    private fun EventChangeListener() {
        db!!.collection("users").whereEqualTo("userType", "Company")
            .addSnapshotListener(EventListener { value, error ->
                if (error != null) {
                    if (progressDialog!!.isShowing) progressDialog!!.dismiss()
                    Toast.makeText(
                        this,
                        "Error " + error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@EventListener
                }
                for (dc in value!!.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        arrayList!!.add(dc.document.toObject(emplist::class.java))
                    }
                    adapter!!.notifyDataSetChanged()
                    if (progressDialog!!.isShowing) progressDialog!!.dismiss()
                }
            })
    }

}