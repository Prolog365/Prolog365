package com.example.prolog365.ui.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prolog365.databinding.RecyclerviewItemCalendarBinding

class CalendarAdapter(val eventList : ArrayList<CalendarData>) : RecyclerView.Adapter<CalendarAdapter.Holder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.Holder {
        val binding = RecyclerviewItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return eventList.count()
    }

    override fun onBindViewHolder(holder: CalendarAdapter.Holder, position: Int) {
        holder.title.text = eventList[position].title
        holder.description.text = eventList[position].description
    }

    inner class Holder(val binding: RecyclerviewItemCalendarBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.calendarEventImage
        val title = binding.calendarEventTitle
        val description = binding.calendarEventDescription

    }


}
