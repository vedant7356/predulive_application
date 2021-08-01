package com.dr.predulive.dashboard.uploadedCourses

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dr.predulive.R
import de.hdodenhof.circleimageview.CircleImageView

class myadapter(var context: Context, var list: MutableList<model>): RecyclerView.Adapter<myadapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<View>(R.id.img1) as CircleImageView
        val name = itemView.findViewById<View>(R.id.nametext) as TextView
        val course = itemView.findViewById<View>(R.id.coursetext) as TextView
        val email = itemView.findViewById<View>(R.id.emailtext) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.singlerow, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mo = list[position]
        holder.name.setText(mo.getTitle())
        holder.course.setText(mo.getMade_by())
        holder.email.setText(mo.getDesc())
        Glide.with(holder.img.getContext()).load(mo.getPurl()).into(holder.img)

        holder.itemView.setOnClickListener { v ->
            val intent = Intent(v.context, Description::class.java)
            intent.putExtra("name", mo.getTitle())
            intent.putExtra("course", mo.getMade_by())
            intent.putExtra("email", mo.getDesc())
            intent.putExtra("video_url", mo.getVideo_url())
            intent.putExtra("pdf_url", mo.getPdf_url())
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}