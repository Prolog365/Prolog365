package com.example.prolog365.ui.Schedule

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prolog365.databinding.ScheduleViewBinding
import com.example.prolog365.db.ScheduleDB
import com.example.prolog365.db.ScheduleEntity
import com.example.prolog365.ui.phonebook.PhonebookData
import com.example.prolog365.ui.phonebook.PhonebookInteraction
import com.example.prolog365.ui.phonebook.phonebook_info.PhonebookInfo
import com.example.prolog365.ui.phonebook.phonebook_info.PhonebookInfoAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleView{
    companion object{
        var scheduleBinding: ScheduleViewBinding? = null
        val binding get() = scheduleBinding!!

        fun showPopupWindow(scheduleData: ScheduleEntity, context: Context){
            CoroutineScope(Dispatchers.IO).launch {
//                PhonebookInfo.binding.tagTextInfoPhonebook?.text = PhonebookInteraction.getFirstLetter(phonebookData.name)
//                PhonebookInfo.binding.nameTextInfoPhonebook?.text = phonebookData.name
//                PhonebookInfo.binding.phonenumberTextInfoPhonebook?.text = phonebookData.phonenumber
//                PhonebookInfo.binding.buttonMessageInfoPhonebook.setOnClickListener{
//                    PhonebookInteraction.sendMessage(phonebookData.phonenumber, context)
//                }
//                PhonebookInfo.binding.buttonPhonecallInfoPhonebook.setOnClickListener{
//                    PhonebookInteraction.sendCall(phonebookData.phonenumber, context)
//                }
//
//
//                var scheduleList = ScheduleDB.getScheduleWithPhonenumber(phonebookData.phonenumber)
//
//                val adapter = PhonebookInfoAdapter(scheduleList as ArrayList<ScheduleEntity>)
//
//                adapter.itemClick = object : PhonebookInfoAdapter.ItemClick{
//                    override fun onClick(view: View, position: Int){
//                        // Add onclick event
//                    }
//                }
//
//                PhonebookInfo.binding.scheduleRecyclerviewInfoPhonebook.adapter = adapter
//                PhonebookInfo.binding.scheduleRecyclerviewInfoPhonebook.layoutManager = LinearLayoutManager(context)
//                val mHandler = Handler(Looper.getMainLooper())
//                mHandler.post(Runnable(){
//                    val popupWindow = PopupWindow(PhonebookInfo.binding.root, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//                    popupWindow.isOutsideTouchable = true
//                    popupWindow.isFocusable = true
//                    popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                    popupWindow.showAtLocation(PhonebookInfo.binding.root, Gravity.CENTER, 0, 0)
//                })
            }

        }

    }

}