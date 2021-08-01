package com.dr.predulive.dashboard.videoReels

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.dr.predulive.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

class VideosAdapter(var context: Context, var videoItems: List<VideoItem>): RecyclerView.Adapter<VideosAdapter.VideoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.item_container_video, parent, false)
        return VideoViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
//        holder.setVideoData(videoItems[position])
        holder.setExoplayer(context, videoItems[position].videoUrl)
    }

    override fun getItemCount(): Int {
        return videoItems.size
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var videoView = itemView.findViewById<View>(R.id.videoView) as PlayerView
        var videoProgressbar: ProgressBar = itemView.findViewById(R.id.videoProgressbar) as ProgressBar


        fun setExoplayer(contextInspire: Context,videoUrl: String) {

            try {
                val player = SimpleExoPlayer.Builder(contextInspire).build()

                videoView.player = player
                var videoUri = videoUrl
                val mediaItem: MediaItem = MediaItem.fromUri(videoUri)
                player.setMediaItem(mediaItem)

                player.prepare()
                videoProgressbar.visibility = View.GONE
                player.play()

            } catch (ex: Exception) {
                Log.d("Exoplayer Crashed", ex.message.toString())
            }
        }
//        fun setVideoData(videoItem: VideoItem) {
//            videoView.setVideoPath(videoItem.videoUrl)
//
//            videoView.setOnPreparedListener {
//                videoProgressbar.visibility = View.GONE
//                it.start()
////                val videoRatio: Float = it.videoWidth.toFloat()  / it.videoHeight.toFloat()
////                val screenRatio: Float = videoView.width.toFloat() / videoView.height.toFloat()
////                val scale: Float = videoRatio / screenRatio
////
////                if(scale >= 1F) {
////                    videoView.scaleX = scale
////                } else {
////                    videoView.scaleY = 1F / scale
////                }
//            }
//
////            videoView.setOnCompletionListener {
////                it.start()
////            }
//        }

    }

}