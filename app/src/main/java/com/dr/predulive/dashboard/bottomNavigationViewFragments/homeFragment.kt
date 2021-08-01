package com.dr.predulive.dashboard.bottomNavigationViewFragments

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.dr.predulive.R
import com.dr.predulive.daos.ShortVideosDao
import com.dr.predulive.dashboard.dashboardButtons.VideoAdapter
import com.dr.predulive.dashboard.sliderHomepage.SlideAdapter
import com.dr.predulive.dashboard.sliderHomepage.SlideItem
import com.dr.predulive.dashboard.videoReels.VideoItem
import com.dr.predulive.dashboard.videoReels.VideosAdapter
import com.dr.predulive.models.ShortVideos
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class homeFragment : Fragment() {

    var db: FirebaseFirestore? = null
    var recyclerView: ViewPager2? = null
    var linearLayoutManager: LinearLayoutManager? = null
    private lateinit var shortVideosDao: ShortVideosDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        db = FirebaseFirestore.getInstance()

        //-------
        recyclerView = view.findViewById<View>(R.id.videosViewPager) as ViewPager2
        shortVideosDao = ShortVideosDao()

        val shortVideoCollection = shortVideosDao.shortVideosCollection
        var videoItems: ArrayList<VideoItem> = ArrayList<VideoItem>()
        var videoAdapter = context?.let { VideosAdapter(it, videoItems) }
        recyclerView!!.adapter = videoAdapter

        shortVideoCollection
            .addSnapshotListener(EventListener { value, error ->
                if (error != null) {
                    return@EventListener
                }
                Toast.makeText(context, value!!.size().toString(), Toast.LENGTH_LONG).show()
                for (dc in value.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        var videoItemCelebration: VideoItem = VideoItem()
                        videoItemCelebration.videoUrl = dc.document.get("videoUrl").toString()
                        videoItems.add(videoItemCelebration)
                        videoAdapter!!.notifyDataSetChanged()
                    }
                }
            })

        //--------
        // slider items

        var items: ArrayList<SlideItem> = ArrayList<SlideItem>()

        var slideAdapter = SlideAdapter(items)
        var recyclerView: ViewPager2 = view.findViewById(R.id.slideRecyclerView) as ViewPager2
//        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = slideAdapter

        db!!.collection("upcomingEvents")
            .addSnapshotListener(EventListener { value, error ->
                if (error != null) {
                    return@EventListener
                }
                for (dc in value!!.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {

                        var item: SlideItem = SlideItem(dc.document.get("title").toString())
                        items.add(item)
//                        var i = arr!!.add(dc.document.toObject(evemodel::class.java))
                    }
                    slideAdapter!!.notifyDataSetChanged()
                }
            })

        return view

    }

}