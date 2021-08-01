package com.dr.predulive.dashboard.dashboardButtons.inspireSection

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dr.predulive.R
import com.dr.predulive.daos.ShortVideosDao
import com.dr.predulive.dashboard.dashboardButtons.UploadShortVideoActivity
import com.dr.predulive.dashboard.dashboardButtons.VideoAdapter
import com.dr.predulive.models.ShortVideos
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query

class InspireActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    var linearLayoutManager: LinearLayoutManager? = null
    private lateinit var shortVideosDao: ShortVideosDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspire)

        init()

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = linearLayoutManager

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("please wait..")
//        progressDialog.show()

        val shortVideoCollection = shortVideosDao.shortVideosCollection
        val query = shortVideoCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val options = FirestoreRecyclerOptions.Builder<ShortVideos>().setQuery(query, ShortVideos::class.java).build()
        val firebaseRecyclerAdapter = VideoAdapter(options, this.applicationContext)



        firebaseRecyclerAdapter.startListening()
        recyclerView!!.adapter = firebaseRecyclerAdapter

    }


    private fun init() {
        recyclerView = findViewById<View>(R.id.recy_entre_podcast) as RecyclerView
        shortVideosDao = ShortVideosDao()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    // upload short Videos
    fun uploadStoriesButtonInspire(view: View) {
        val intent = Intent(this, UploadShortVideoActivity::class.java)
        startActivity(intent)
    }


}