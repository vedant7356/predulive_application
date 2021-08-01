package com.dr.predulive.dashboard.bottomNavigationViewFragments

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dr.predulive.R
import com.dr.predulive.dashboard.dashboardButtons.VideoAdapter
import com.dr.predulive.models.ShortVideos
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView


class HomeVideoAdapter(options: FirestoreRecyclerOptions<ShortVideos>, applicationContext: Context) : FirestoreRecyclerAdapter<ShortVideos, HomeVideoAdapter.VideoViewHolder>(
    options
) {

    val applicationContext = applicationContext

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.entr_podcast_title)
        val videoView = itemView.findViewById<View>(R.id.en_podcast_explore) as PlayerView
        val videoDescription = itemView.findViewById<View>(R.id.videoDescription) as TextView
        private lateinit var context: Context

        fun setExoplayer(contextInspire: Context, tile: String?, videoUrl: String, videoDescriptionData: String) {
            context = contextInspire

            try {
                title.text = tile
                videoDescription.text = videoDescriptionData
                val player = SimpleExoPlayer.Builder(context).build()

                videoView.player = player
                var videoUri = videoUrl
                val mediaItem: MediaItem = MediaItem.fromUri(videoUri)
                player.setMediaItem(mediaItem)

//                player.prepare()
//                player.play()

            } catch (ex: Exception) {
                Log.d("Exoplayer Crashed", ex.message.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val viewHolder = VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.entre_podcast_recy_layout_file, parent, false))

        return viewHolder
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int, model: ShortVideos) {
        holder.title.text = model.videoTitle
        holder.setExoplayer(applicationContext, model.videoTitle, model.videoUrl, model.videoDescription)
    }



}