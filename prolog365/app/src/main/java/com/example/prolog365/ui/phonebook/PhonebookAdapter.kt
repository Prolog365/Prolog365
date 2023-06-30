package com.example.prolog365.ui.phonebook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prolog365.databinding.RecyclerviewItemPhonebookBinding

class PhonebookAdapter(val phonebookList : ArrayList<PhonebookData>) : RecyclerView.Adapter<PhonebookAdapter.Holder>(){

    interface ItemClick{
        fun onClick(view: View, position : Int)
    }

    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhonebookAdapter.Holder {
        val binding = RecyclerviewItemPhonebookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: PhonebookAdapter.Holder, position: Int) {
        holder.itemView.setOnClickListener{
            itemClick?.onClick(it, position)
        }
        holder.name.text = phonebookList[position].name
        holder.phonenumber.text = phonebookList[position].phonenumber
    }

    override fun getItemCount(): Int {
        return phonebookList.size
    }



    inner class Holder(val binding: RecyclerviewItemPhonebookBinding) : RecyclerView.ViewHolder(binding.root){
        val name = binding.nameTextItemPhonebook
        val phonenumber = binding.phonenumberTextItemPhonebook

    }

}