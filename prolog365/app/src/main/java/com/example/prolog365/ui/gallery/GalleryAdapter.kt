package com.example.prolog365.ui.gallery

import android.content.Context
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.Gallery
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import androidx.core.net.toUri
import androidx.core.view.marginTop
import androidx.core.view.setPadding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

class GalleryAdapter(private val context: FragmentActivity?, private val imageList: List<GalleryData>) : BaseAdapter() {


//    interface ItemClick{
//        fun onClick(view: View, position : Int)
//    }
//
//    var itemClick : ItemClick? = null

    override fun getCount(): Int {
        return imageList.size
    }

    override fun getItem(position: Int): Any {
        return imageList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun clickItemGallery(context: Context, galleryData: GalleryData){
        Log.d("MyLog", "Click!: " + galleryData.imageSource)
        if (context != null) {

            GalleryShow.binding?.imageViewShowGallery?.let {
                Glide.with(context)
                    .load(galleryData.imageSource.toUri())
                    .into(it)
            }

            GalleryShow.showPopupWindow(galleryData.imageSource, context)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val galleryViewModel = context?.let { ViewModelProvider(it, ViewModelProvider.NewInstanceFactory()).get(GalleryViewModel::class.java) }
        val imageViewList = galleryViewModel?.imageViewList
        var imageView: ImageView
        if (imageViewList != null) {
            while(imageViewList.size <= position){
                val idx = imageViewList.size
                imageView = ImageView(context)
                imageView.setOnClickListener{
                    clickItemGallery(imageView.context, imageList[position])
                }
                imageView.layoutParams = RelativeLayout.LayoutParams(290, 290)
                val layoutParams = imageView.layoutParams as RelativeLayout.LayoutParams
                imageView.layoutParams = layoutParams
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                Log.d("MyLog", imageList[position].imageSource)
                //imageView.setImageURI(imageList[position].imageSource.toUri())
                val imagePath = imageList[position].imageSource
                val bitmap = BitmapFactory.decodeFile(imagePath)
                Glide.with(imageView.context)
                    .load(bitmap)
                    .into(imageView)

                imageViewList.add(imageView)
            }
        }
        return imageViewList?.get(position) ?: convertView

//        if (convertView == null) {
//            imageView = ImageView(context)
//            imageView.layoutParams = RelativeLayout.LayoutParams(290, 290)
//            val layoutParams = imageView.layoutParams as RelativeLayout.LayoutParams
//            imageView.layoutParams = layoutParams
//            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
//        } else {
//            imageView = convertView as ImageView
//        }

        // Set the image for the ImageView using the Uri



        //return imageView
    }
}
