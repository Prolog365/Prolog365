package com.example.prolog365.ui.gallery

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Gravity
import android.view.ViewGroup
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.example.prolog365.databinding.ShowGalleryBinding
import com.example.prolog365.ui.phonebook.PhonebookFragment
import com.example.prolog365.ui.phonebook.phonebook_add.PhonebookAdd

class GalleryShow {
    companion object{
        var _binding: ShowGalleryBinding? = null
        var popupWindow: PopupWindow? = null

        private val binding get() = _binding

        fun showPopupWindow(imageUri: Uri, context: Context){
            binding?.imageViewShowGallery?.let { Glide.with(context).load(imageUri).into(it)}
            //binding?.imageViewShowGallery?.setImageURI(imageUri)
            popupWindow = PopupWindow(binding?.root, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            popupWindow!!.isOutsideTouchable = true
            popupWindow!!.isFocusable = true
            popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow!!.showAtLocation(binding?.root, Gravity.CENTER, 0, 0)
        }

    }

}