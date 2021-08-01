package com.dr.predulive.dashboard.bottomNavigationViewFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dr.predulive.R
import com.dr.predulive.dashboard.uploadedCourses.model
import com.dr.predulive.dashboard.uploadedCourses.myadapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_courses.view.*

import java.util.*

class coursesFragment : Fragment() {

    var recview: RecyclerView? = null
    var adapter: myadapter? = null
    var databaseReference: DatabaseReference? = null
    var arrayList: ArrayList<model>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    private fun processsearch(s: String) {
        val list = ArrayList<model>()
        for (ob in arrayList!!) {
            if (ob.getTitle().toLowerCase().contains(s.toLowerCase())) {
                list.add(ob)
            }
        }
        val myadapter = context?.let { myadapter(it, list) }
        recview!!.adapter = myadapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_courses, container, false)

        recview = view.findViewById<View>(R.id.recview1) as RecyclerView
        databaseReference = FirebaseDatabase.getInstance().reference.child("students")
        recview!!.layoutManager = LinearLayoutManager(context)


        arrayList = ArrayList()

        adapter = context?.let { myadapter(it, arrayList!!) }
        recview!!.adapter = adapter

        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var id = snapshot.childrenCount.toInt()
                }
                for (dataSnapshot in snapshot.children) {
                    val mo: model? = dataSnapshot.getValue(model::class.java) as model?
                    arrayList!!.add(mo!!)
                }
                adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })


        view.coursesSearchView1.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
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
        return view
    }

}