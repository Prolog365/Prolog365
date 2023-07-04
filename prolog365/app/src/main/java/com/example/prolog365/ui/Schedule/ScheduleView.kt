package com.example.prolog365.ui.Schedule

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import androidx.viewpager2.widget.ViewPager2
import com.example.prolog365.R
import com.example.prolog365.databinding.InfoPhonebookBinding
import com.example.prolog365.databinding.ScheduleViewBinding
import com.example.prolog365.db.PhonebookDB
import com.example.prolog365.db.ScheduleEntity
import com.example.prolog365.ui.phonebook.PhonebookInteraction
import com.example.prolog365.ui.phonebook.phonebook_info.PhonebookInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Math.abs

class ScheduleView{
    companion object{
        var scheduleBinding: ScheduleViewBinding? = null
        val binding get() = scheduleBinding!!

        fun setImages(scheduleData: ScheduleEntity){
            var bgColors = arrayListOf<Int>(
                R.color.black,
                R.color.black,
                R.color.black,
                R.color.black,
                R.color.black
            )
            binding.galleryViewpagerScheduleView.adapter = GalleryPagerRecyclerAdapter(scheduleData)
            binding.galleryViewpagerScheduleView.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            binding.galleryViewpagerScheduleView.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                // Paging 완료되면 호출
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.d("ViewPagerFragment", "Page ${position+1}")
                }
            })
            binding.galleryViewpagerScheduleView.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.e("ViewPagerFragment", "Page ${position+1}")
                }
            })
        }

        @SuppressLint("ServiceCast")
        fun showPopupWindow(scheduleData: ScheduleEntity){
            CoroutineScope(Dispatchers.IO).launch {
                binding.dateTextScheduleView.text = scheduleData.date
                binding.titleTextScheduleView.setText(scheduleData.scheduleName)
                val phonebookData = PhonebookDB.findPhonebookDataByNumber(scheduleData.phoneNumber)
                binding.tagTextScheduleView.text = phonebookData?.name?.let {
                    PhonebookInteraction.getFirstLetter(
                        it
                    )
                }
                binding.nameTextScheduleView.text = phonebookData?.name
                binding.titleTextScheduleView.setOnEditorActionListener() {_, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
                    ) {
                        val inputMethodManager = binding.root.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(binding.root.getWindowToken(), 0)
                        binding.titleTextScheduleView.clearFocus()
                        // on below line hiding our keyboard.
                        true // Return true to indicate that the event has been handled
                    } else {
                        false // Return false to allow default behavior for other actions
                    }
                }



                setImages(scheduleData)

//
//                binding.scheduleRecyclerviewInfoPhonebook.adapter = adapter
//                binding.scheduleRecyclerviewInfoPhonebook.layoutManager = LinearLayoutManager(context)
                val mHandler = Handler(Looper.getMainLooper())
                mHandler.post(Runnable(){
                    val popupWindow = PopupWindow(binding.root, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    popupWindow.isOutsideTouchable = true
                    popupWindow.isFocusable = true
                    popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
                })
            }

        }

    }

}