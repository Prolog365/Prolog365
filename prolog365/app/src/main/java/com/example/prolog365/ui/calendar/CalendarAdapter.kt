package com.example.prolog365.ui.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.prolog365.databinding.RecyclerviewItemCalendarBinding

class CalendarAdapter(val eventList : ArrayList<CalendarData>) : RecyclerView.Adapter<CalendarAdapter.Holder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.Holder {
        val binding = RecyclerviewItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val event = eventList[position]
        holder.bind(event)
    }

    inner class Holder(private val binding: RecyclerviewItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: CalendarData) {
            binding.calendarEventTitle.text = event.title
            binding.calendarEventPhoneNum.text = event.phoneNum

            Glide.with(binding.root)
                .load(event.image)
                .into(binding.calendarEventImage)
        }
    }


}
