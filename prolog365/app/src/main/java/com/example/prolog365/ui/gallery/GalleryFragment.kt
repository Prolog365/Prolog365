package com.example.prolog365.ui.gallery

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.prolog365.R
import com.example.prolog365.databinding.FragmentGalleryBinding
import com.example.prolog365.databinding.ScheduleViewBinding
import com.example.prolog365.databinding.ShowGalleryBinding
import com.example.prolog365.db.ScheduleDB
import com.example.prolog365.ui.Schedule.ScheduleView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    public val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        GalleryShow._binding = ShowGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        ScheduleView.scheduleBinding = ScheduleViewBinding.inflate(inflater, null, false)

        if(binding.gridViewGallery.adapter==null){
            setGridViewGallery()
        }
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_gallery, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_gallery -> {
                // Handle the "add button" selection
                addGallery()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private val REQUEST_CODE_GALLERY  = 30
    private fun addGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }


    fun extractDigitCharacters(originalString: String): String {
        val regex = Regex("[^\\d]+")
        return originalString.replace(regex, "")
    }
    fun getImageAbsolutePath(context: Context, uri: Uri): String? {
        val cacheDir = context.cacheDir
        val fileName = uri.path?.let { extractDigitCharacters(it) } + ".png"
        val destinationFile = File(cacheDir, fileName)
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val outputStream = FileOutputStream(destinationFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()

        val filePath = destinationFile.absolutePath

        return filePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            // Process the selected image URI as needed
            context?.let {
                if (selectedImageUri != null) {
                    val picture = getImageAbsolutePath(it, selectedImageUri)
                    CoroutineScope(Dispatchers.IO).launch {
                        if (picture != null) {
                            ScheduleDB.insertDB("MySchedule", LocalDate.now(), "010-1234-5678", picture)
                        }
                        ScheduleDB.logDB()
                        setGridViewGallery()
                        showToast("이미지가 추가되었습니다")
                    }
                }
            }

        }
    }

    private fun showToast(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    fun setGridViewGallery(){
        CoroutineScope(Dispatchers.IO).launch{
            val scheduleList = ScheduleDB.getEverySchedule()
                /*var galleryList = ArrayList<ScheduleEntity>()
            for(schedule in scheduleList!!){
                val galleryData = GalleryData(schedule.picture, schedule.picture)
                galleryList.add(galleryData)
            }*/
            val adapter = scheduleList?.let { GalleryAdapter(activity, it) }

//            adapter.itemClick = object : GalleryAdapter.ItemClick{
//
//                override fun onClick(view: View, position: Int){
//                    Log.d("MyLog", "itemClick")
//                    clickItemGallery(galleryList[position])
//                }
//            }

            val handler = Handler(Looper.getMainLooper())
            handler.post {
                binding.gridViewGallery.adapter = adapter
            }
        }
    }

    /*fun clickItemGallery(galleryData: GalleryData){

    }



    fun setRecyclerViewGallery(){
        CoroutineScope(Dispatchers.IO).launch{
            val scheduleList = ScheduleDB.getEverySchedule()
            var galleryList = ArrayList<GalleryData>()
            for(schedule in scheduleList!!){
                val galleryData = GalleryData(schedule.picture, schedule.picture)
                galleryList.add(galleryData)
            }
            val adapter = GalleryAdapter(galleryList)

            adapter.itemClick = object : GalleryAdapter.ItemClick{
                override fun onClick(view: View, position: Int){
                    clickItemGallery(galleryList[position])
                }
            }

            val handler = Handler(Looper.getMainLooper())
            handler.post {
                binding.recyclerviewGallery.adapter = adapter
                binding.recyclerviewGallery.layoutManager = LinearLayoutManager(activity)
            }
        }
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}