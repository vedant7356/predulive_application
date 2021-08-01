package com.dr.predulive.dashboard.sliderHomepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dr.predulive.R

class SlideAdapter(var items: List<SlideItem>): RecyclerView.Adapter<SlideAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.slide_item, parent, false)
        return ItemViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.slideItem.text = items[position].title
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var slideItem: TextView = itemView.findViewById(R.id.slideItem)
    }

}