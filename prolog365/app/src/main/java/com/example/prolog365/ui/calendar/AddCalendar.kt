package com.example.prolog365.ui.calendar

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.prolog365.R
import com.example.prolog365.db.ScheduleDB
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale


class AddCalendar : BottomSheetDialogFragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var calendarViewModel: CalendarViewModel
    private lateinit var imageUri: Uri
    private lateinit var phoneNumber: String

    companion object {
        private const val IMAGE_PICK_REQUEST_CODE = 100
        private const val STORAGE_PERMISSION_REQUEST_CODE = 101
        private const val CONTACT_PERMISSION_REQUEST_CODE = 102
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_calendar, container, false)

        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)

        val pickPhoneNumberButton: Button = view.findViewById(R.id.add_calendar_btn_phone_num)
        pickPhoneNumberButton.setOnClickListener {
            checkContactPermission()
        }

        val pickDateButton: Button = view.findViewById(R.id.add_calendar_btn_date)
        pickDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        val pickPictureButton: Button = view.findViewById(R.id.add_calendar_btn_picture)
        pickPictureButton.setOnClickListener {
            checkStoragePermission()
        }

        val submitButton: Button = view.findViewById(R.id.add_calendar_submit_button)
        submitButton.setOnClickListener {
            submitData()
        }

        return view
    }

    private val imagePickLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
                val imagePickButton: Button = requireView().findViewById(R.id.add_calendar_btn_picture)
                imagePickButton.text = "$imageUri"
            }
        }
    private fun checkStoragePermission() {
        val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(requireContext(), readPermission) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), writePermission) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(readPermission, writePermission),
                STORAGE_PERMISSION_REQUEST_CODE
            )
        }
        else {
            openGallery()
        }
    }
    private fun openGallery() {
        imagePickLauncher.launch("image/*")
    }
    private fun checkContactPermission() {
        val contactPermission = Manifest.permission.READ_CONTACTS

        if(ContextCompat.checkSelfPermission(requireContext(), contactPermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(contactPermission),
                CONTACT_PERMISSION_REQUEST_CODE
            )
        }
        else {
            openContact()
        }
    }

    private val contactPickLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result: ActivityResult? ->
        if (result?.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val contactUri = data?.data
            val phoneNumber = getPhoneNumber(contactUri)

            if(phoneNumber.isNotEmpty()) {
                setPhoneNumber(phoneNumber)
            }
            else {
                showToast("Failed to retrieve phone number from contact")
            }
        }
    }

    private fun getPhoneNumber(contactUri: Uri?): String {
        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)

        val selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
        val contactId = contactUri?.lastPathSegment
        val selectionArgs = arrayOf(contactId)

        val cursor = requireContext().contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, selection, selectionArgs, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val column = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                if(column>=0){
                    do {
                        val phoneNumber = it.getString(column)
                        if (phoneNumber != null && phoneNumber.isNotEmpty()) {
                            return phoneNumber
                        }
                    } while (it.moveToNext())
                }
            }
        }
        return ""
    }


    private fun setPhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
        val phoneNumberButton: Button = requireView().findViewById(R.id.add_calendar_btn_phone_num)
        phoneNumberButton.text = phoneNumber
    }

    private fun openContact(){
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        contactPickLauncher.launch(intent)
    }

    private fun submitData() {
        val eventNameEditText: EditText = requireView().findViewById(R.id.add_calendar_edit_name)
        val eventName = eventNameEditText.text.toString().trim()

        if (eventName.isEmpty()) {
            showToast("Please fill in all the fields")
            return
        }
        val selectedDate = getSelectedDate()
        if (selectedDate == null) {
            showToast("Please select a date")
            return
        }

        context?.let { cit ->
            getImageAbsolutePath(cit, imageUri)?.let {
                insertSchedule(eventName,phoneNumber,selectedDate,
                    it
                )
            }
        }
        dismiss()
    }

    private fun getSelectedDate(): LocalDate? {
        val pickDateButton: Button = requireView().findViewById(R.id.add_calendar_btn_date)
        val selectedDateText = pickDateButton.text.toString()

        return try {
            LocalDate.parse(selectedDateText)
        } catch (e: Exception) {
            null
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), this, year,month, day)
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = Calendar.getInstance()
        selectedDate.set(Calendar.YEAR, year)
        selectedDate.set(Calendar.MONTH, month)
        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate.time)

        val pickDateButton: Button = requireView().findViewById(R.id.add_calendar_btn_date)
        pickDateButton.text = formattedDate
    }
    private fun insertSchedule(scheduleName: String, phoneNumber: String, date: LocalDate, picture: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ScheduleDB.insertDB(scheduleName, date, phoneNumber, picture)
                ScheduleDB.logDB()
                //showToast("Data submitted successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error inserting schedule", e)
                //showToast("Failed to submit data")
            }
        }
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


    private fun showToast(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_REQUEST_CODE) {
            data?.data?.let { uri ->
                imageUri = uri
                val contentResolver = context?.contentResolver

                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
// Check for the freshest data.
                if (contentResolver != null) {
                    contentResolver.takePersistableUriPermission(imageUri, takeFlags)
                }
                showToast("Image selected: $imageUri")
            }
        }
    }
}