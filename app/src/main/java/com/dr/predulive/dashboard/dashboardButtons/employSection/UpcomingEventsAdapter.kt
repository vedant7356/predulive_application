package com.dr.predulive.dashboard.dashboardButtons.employSection

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dr.predulive.R
import com.dr.predulive.models.evemodel
import java.util.*

class UpcomingEventsAdapter(var context: Context, var list: ArrayList<evemodel>): RecyclerView.Adapter<UpcomingEventsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById<View>(R.id.title_event) as TextView
        var details = itemView.findViewById<View>(R.id.details_event) as TextView
        var joining_url = itemView.findViewById<View>(R.id.joining_url) as TextView
        var date = itemView.findViewById<View>(R.id.date) as TextView
//        var joining_url.setMovementMethod(LinkMovementMethod.getInstance())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.upcoming_events_item,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.joining_url.movementMethod = LinkMovementMethod.getInstance()
        val mo = list[position]
        holder.title.text = mo.title
        holder.details.text = mo.details_Of_Events
        holder.joining_url.text = mo.joining_url
        holder.date.text = mo.getDate()

        holder.itemView.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mo.joining_url))
            context.startActivity(browserIntent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}