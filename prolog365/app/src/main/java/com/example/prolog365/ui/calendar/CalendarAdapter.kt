package com.example.prolog365.ui.calendar

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.prolog365.databinding.RecyclerviewItemCalendarBinding
import com.example.prolog365.db.ScheduleEntity
import com.example.prolog365.ui.Schedule.ScheduleView

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

    fun clickItemCalender(calenderData: CalendarData){
        ScheduleView.showPopupWindow(ScheduleEntity(0, calenderData.title, calenderData.date.toString(), calenderData.phoneNum, calenderData.image))
    }
    inner class Holder(private val binding: RecyclerviewItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: CalendarData) {
            binding.root.setOnClickListener{
                clickItemCalender(event)
            }
            binding.calendarEventTitle.text = event.title
            binding.calendarEventPhoneNum.text = event.phoneNum
            val imagePath = event.image
            val bitmap = BitmapFactory.decodeFile(imagePath)
            Glide.with(binding.root)
                .load(bitmap)
                .into(binding.calendarEventImage)
        }
    }


}
