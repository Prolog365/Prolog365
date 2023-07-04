package com.example.prolog365.ui.gallery

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.prolog365.databinding.ShowGalleryBinding
import com.example.prolog365.ui.phonebook.PhonebookFragment
import com.example.prolog365.ui.phonebook.phonebook_add.PhonebookAdd

class GalleryShow {
    companion object{
        var _binding: ShowGalleryBinding? = null
        var popupWindow: PopupWindow? = null

        val binding get() = _binding

        fun showPopupWindow(imagePath: String, context: Context){


            val bitmap = BitmapFactory.decodeFile(imagePath)
            binding?.imageViewShowGallery?.let {
                Glide.with(context)
                    .load(bitmap)
                    .into(it)
            }
            /*val inputStream=context.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding?.imageViewShowGallery?.setImageBitmap(bitmap)

             */
            ///////binding?.imageViewShowGallery?.let { Glide.with(context).load(imageUri).into(it) }



            //binding?.imageViewShowGallery?.let { Glide.with(context).load(bitmap).into(it)}
            //binding?.imageViewShowGallery?.setImageURI(imageUri)
            popupWindow = PopupWindow(binding?.root, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            popupWindow!!.isOutsideTouchable = true
            popupWindow!!.isFocusable = true
            popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow!!.showAtLocation(binding?.root, Gravity.CENTER, 0, 0)
        }

    }

}