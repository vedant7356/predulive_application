package com.dr.predulive.dashboard.dashboardButtons.employSection

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dr.predulive.R
import com.dr.predulive.models.evemodel
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*

class UpcomingEventsActivity : AppCompatActivity() {

    var arr: ArrayList<evemodel>? = null
    var recview: RecyclerView? = null
    var adapter: UpcomingEventsAdapter? = null
    var db: FirebaseFirestore? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_events)


        progressDialog = ProgressDialog(this)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Fetching Data")
        progressDialog!!.show()
        recview = findViewById<View>(R.id.upcomingEventsRecyclerView) as RecyclerView
        recview!!.setHasFixedSize(true)
        recview!!.layoutManager = LinearLayoutManager(this)

        db = FirebaseFirestore.getInstance()
        arr = ArrayList<evemodel>()

        adapter = UpcomingEventsAdapter(this, arr!!)


        recview!!.adapter = adapter

        EventChangeListener()

        Toast.makeText(this, "Fetching Upcoming Events", Toast.LENGTH_SHORT).show()
//        var e = evemodel("details of event", "joining url", "title", "data")
//        db!!.collection("upcomingEvents").document().set(e)

    }

    private fun EventChangeListener() {

        db!!.collection("upcomingEvents")
            .addSnapshotListener(EventListener { value, error ->
                if (error != null) {
                    if (progressDialog!!.isShowing) progressDialog!!.dismiss()
                    Toast.makeText(this, "Error " + error.message, Toast.LENGTH_SHORT).show()
                    return@EventListener
                }
                for (dc in value!!.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        arr!!.add(dc.document.toObject(evemodel::class.java))
                    }
                    adapter!!.notifyDataSetChanged()
                    if (progressDialog!!.isShowing) progressDialog!!.dismiss()
                }
            })
    }
}