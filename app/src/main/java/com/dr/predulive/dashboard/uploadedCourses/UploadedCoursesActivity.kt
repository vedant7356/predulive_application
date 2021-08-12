package com.dr.predulive.dashboard.uploadedCourses

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.dr.predulive.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_uploaded_courses.*
import java.util.*

class UploadedCoursesActivity : AppCompatActivity() {

//    var recview: RecyclerView? = null
//    var adapter: myadapter? = null
//    var databaseReference: DatabaseReference? = null
//    lateinit var arrayList: MutableList<model>
//    var addvideo: FloatingActionButton? = null

    var recview: RecyclerView? = null
    var adapter: myadapter? = null
    var databaseReference: DatabaseReference? = null
    var arrayList: ArrayList<model>? = null
    var addvideo: FloatingActionButton? = null
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uploaded_courses)


//        recview = findViewById<View>(R.id.recview) as RecyclerView
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("students")
//        recview!!.layoutManager = LinearLayoutManager(this)
//        addvideo = findViewById<View>(R.id.floatingActionButton) as FloatingActionButton
//        addvideo!!.setOnClickListener {
//            startActivity(
//                Intent(
//                    applicationContext,
//                    Create_new_courses::class.java
//                )
//            )
//        }
//
//        arrayList = arrayListOf()
//
//        adapter = myadapter(this, arrayList)
//        recview!!.adapter = adapter
//
//        databaseReference!!.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (dataSnapshot in snapshot.getChildren()) {
//                    val mo: model? = dataSnapshot.getValue(model::class.java)
//                    if (mo != null) {
//                        arrayList!!.add(mo)
//                    }
//                }
//                adapter!!.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//        })

        recview = findViewById<View>(R.id.recview) as RecyclerView
        databaseReference = FirebaseDatabase.getInstance().reference.child("students")
        recview!!.layoutManager = LinearLayoutManager(this)
        addvideo = findViewById<View>(R.id.floatingActionButton) as FloatingActionButton
        addvideo!!.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    Create_new_courses::class.java
                )
            )
        }

        arrayList = ArrayList()

        adapter = myadapter(this, arrayList!!)
        recview!!.adapter = adapter

        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    id = snapshot.childrenCount.toInt()
                }
                for (dataSnapshot in snapshot.children) {
                    val mo: model? = dataSnapshot.getValue(model::class.java)
                    arrayList!!.add(mo!!)
                }
                adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        databaseReference!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                notification()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })


        coursesSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                processsearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                processsearch(newText)
                return false
            }

        })


    }


    private fun notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService<NotificationManager>(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(this, "n")
            .setContentText("Predulive Edutech Foundation")
            .setSmallIcon(R.drawable.exo_notification_small_icon)
            .setAutoCancel(true)
            .setContentText("New Event Is Added")
        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(999, builder.build())
    }



    private fun processsearch(s: String) {
        val list = ArrayList<model>()
        for (ob in arrayList!!) {
            if (ob.getTitle().toLowerCase().contains(s.toLowerCase())) {
                list.add(ob)
            }
        }
        val myadapter = myadapter(this, list)
        recview!!.adapter = myadapter
    }

}