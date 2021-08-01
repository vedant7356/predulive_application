package com.dr.predulive.models

class ShortVideos(
    var videoTitle: String = "",
    var videoUrl: String = "",
    var videoDescription: String = "",
    var createdBy: User = User(),
    var createdAt: Long = 0L,
    var likedBy: ArrayList<String> = ArrayList()
) {

}