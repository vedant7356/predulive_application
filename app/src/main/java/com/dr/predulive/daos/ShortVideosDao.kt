package com.dr.predulive.daos

import com.dr.predulive.models.ShortVideos
import com.dr.predulive.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ShortVideosDao {
    val db = FirebaseFirestore.getInstance()
    val shortVideosCollection = db.collection("shortVideos")
    val auth = Firebase.auth

    fun addShortVideo(videoTitle: String, videoUrl: String, videoDescription: String) {
        GlobalScope.launch(Dispatchers.IO) {

            val uid: String = auth.currentUser?.uid.toString()
            var userDao = UserDao()
            val user: User = userDao.getUserById(uid).await().toObject(User::class.java)!!
            val currentTime = System.currentTimeMillis()

            var shortVideo = ShortVideos(
                videoTitle,
                videoUrl,
                videoDescription,
                user,
                currentTime
            )
            shortVideosCollection.document().set(shortVideo)
        }
    }
}