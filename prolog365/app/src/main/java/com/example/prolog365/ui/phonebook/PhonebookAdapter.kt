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

    companion object{

    }

    override fun onBindViewHolder(holder: PhonebookAdapter.Holder, position: Int) {
        holder.itemView.setOnClickListener{
            itemClick?.onClick(it, position)
        }
        holder.tag.text = PhonebookInteraction.getFirstLetter(phonebookList[position].name)
        holder.name.text = phonebookList[position].name
        //holder.phonenumber.text = phonebookList[position].phonenumber
        holder.messageBtn.setOnClickListener{
            PhonebookInteraction.sendMessage(phonebookList[position].phonenumber, holder.itemView.context)
        }
        holder.callBtn.setOnClickListener{
            PhonebookInteraction.sendCall(phonebookList[position].phonenumber, holder.itemView.context)
        }

    }

    override fun getItemCount(): Int {
        return phonebookList.size
    }



    inner class Holder(val binding: RecyclerviewItemPhonebookBinding) : RecyclerView.ViewHolder(binding.root){
        val tag = binding.tagTextItemPhonebook
        val name = binding.nameTextItemPhonebook
        val messageBtn = binding.buttonMessagePhonebook
        val callBtn = binding.buttonPhonecallPhonebook
        //val phonenumber = binding.phonenumberTextItemPhonebook

    }

}