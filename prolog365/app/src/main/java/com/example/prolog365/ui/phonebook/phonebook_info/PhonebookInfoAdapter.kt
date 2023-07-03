package com.example.prolog365.ui.phonebook.phonebook_info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prolog365.databinding.RecyclerviewItemInfoPhonebookBinding
import com.example.prolog365.databinding.RecyclerviewItemPhonebookBinding
import com.example.prolog365.db.ScheduleEntity

class PhonebookInfoAdapter(val scheduleList : ArrayList<ScheduleEntity>) : RecyclerView.Adapter<PhonebookInfoAdapter.Holder>(){

    interface ItemClick{
        fun onClick(view: View, position : Int)
    }

    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhonebookInfoAdapter.Holder {
        val binding = RecyclerviewItemInfoPhonebookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: PhonebookInfoAdapter.Holder, position: Int) {
        holder.itemView.setOnClickListener{
            itemClick?.onClick(it, position)
        }
        holder.name.text = scheduleList[position].scheduleName


        holder.date.text = scheduleList[position].date

    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }



    inner class Holder(val binding: RecyclerviewItemInfoPhonebookBinding) : RecyclerView.ViewHolder(binding.root){
        val name = binding.nameTextItemInfoPhonebook
        val date = binding.dateTextItemInfoPhonebook
    }

}