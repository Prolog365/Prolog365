package com.example.prolog365.ui.phonebook.phonebook_info

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prolog365.databinding.InfoPhonebookBinding
import com.example.prolog365.db.ScheduleDB
import com.example.prolog365.db.ScheduleEntity
import com.example.prolog365.ui.phonebook.PhonebookData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhonebookInfo(){
    companion object{
        var _infobinding: InfoPhonebookBinding? = null
        private val binding get() = _infobinding!!

        fun showPopupWindow(phonebookData: PhonebookData, context: Context){
            CoroutineScope(Dispatchers.IO).launch {
                binding.nameTextInfoPhonebook?.text = phonebookData.name
                binding.phonenumberTextInfoPhonebook?.text = phonebookData.phonenumber

                val scheduleList = ScheduleDB.getScheduleWithPhonenumber(phonebookData.phonenumber)
                val adapter = PhonebookInfoAdapter(scheduleList as ArrayList<ScheduleEntity>)

                adapter.itemClick = object : PhonebookInfoAdapter.ItemClick{
                    override fun onClick(view: View, position: Int){
                        // Add onclick event
                    }
                }

                binding.scheduleRecyclerviewInfoPhonebook.adapter = adapter
                binding.scheduleRecyclerviewInfoPhonebook.layoutManager = LinearLayoutManager(context)


            }
            val popupWindow = PopupWindow(binding.root, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
        }

    }
}

