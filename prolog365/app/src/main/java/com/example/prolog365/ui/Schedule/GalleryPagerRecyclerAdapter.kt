package com.example.prolog365.ui.Schedule

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.prolog365.R
import com.example.prolog365.databinding.GalleryItemScheduleViewBinding
import com.example.prolog365.databinding.RecyclerviewItemPhonebookBinding
import com.example.prolog365.db.ScheduleEntity

class GalleryPagerRecyclerAdapter(private val scheduleData: ScheduleEntity) : RecyclerView.Adapter<GalleryPagerRecyclerAdapter.Holder>() {
    var context: Context?=null


    /*inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView =

        private val pageName: TextView = itemView.findViewById(R.id.pageName)

        fun bind(@ColorRes bgColor: Int, position: Int) {
            pageName.text = "Page ${position+1}"
            pageName.setBackgroundColor(ContextCompat.getColor(pageName.context, bgColor))
        }
    }*/

    inner class Holder(val binding: GalleryItemScheduleViewBinding) : RecyclerView.ViewHolder(binding.root){
        val imageView = binding.imageViewScheduleView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = GalleryItemScheduleViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return Holder(binding)
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        Log.d("MyLog", scheduleData.picture)
        //holder.imageView.setImageURI(scheduleData.picture.toUri())
        context?.let { Glide.with(it).load(scheduleData.picture).into(holder.imageView) }
    }

    override fun getItemCount(): Int = 3
}
