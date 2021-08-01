package com.dr.predulive.dashboard.dashboardButtons.employSection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dr.predulive.R
import com.dr.predulive.models.emplist
import java.util.*

class EmployerListAdapter(var context: Context, var list: ArrayList<emplist>): RecyclerView.Adapter<EmployerListAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var img = itemView.findViewById<View>(R.id.img1) as de.hdodenhof.circleimageview.CircleImageView
        var name = itemView.findViewById<View>(R.id.nametext) as TextView
        var email = itemView.findViewById<View>(R.id.emailtext) as TextView
        var usertypetxt = itemView.findViewById<View>(R.id.usertype) as TextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployerListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.employer_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployerListAdapter.MyViewHolder, position: Int) {
        val mo = list[position]
        holder.name.text = mo.getDisplayName()
        holder.email.text = mo.getEmail()
        holder.usertypetxt.text = mo.getUserType()
        Glide.with(holder.img.context).load(mo.getImageUrl()).into(holder.img)

    }

    override fun getItemCount(): Int {
        return list.size
    }

}