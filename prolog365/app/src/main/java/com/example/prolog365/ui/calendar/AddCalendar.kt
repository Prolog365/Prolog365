package com.example.prolog365.ui.calendar

import android.Manifest
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.prolog365.R
import com.example.prolog365.db.PhonebookDB
import com.example.prolog365.db.ScheduleDB
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

class AddCalendar : BottomSheetDialogFragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var calendarViewModel: CalendarViewModel
    private lateinit var imageUri: Uri

    companion object {
        private const val IMAGE_PICK_REQUEST_CODE = 100
        private const val STORAGE_PERMISSION_REQUEST_CODE = 101
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_calendar, container, false)

        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)

        val pickPictureButton: Button = view.findViewById(R.id.add_calendar_btn_picture)
        pickPictureButton.setOnClickListener {
            checkStoragePermission()
        }

        val submitButton: Button = view.findViewById(R.id.add_calendar_submit_button)
        submitButton.setOnClickListener {
            submitData()
        }

        val pickDateButton: Button = view.findViewById(R.id.add_calendar_btn_date)
        pickDateButton.setOnClickListener {
            showDatePickerDialog()
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

    private fun openGallery() {
        imagePickLauncher.launch("image/*")
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
        } else {
            openGallery()
        }
    }

    private fun submitData() {
        val eventNameEditText: EditText = requireView().findViewById(R.id.add_calendar_edit_name)
        val phoneNumberEditText: EditText = requireView().findViewById(R.id.add_calendar_edit_phone)

        val eventName = eventNameEditText.text.toString().trim()
        val phoneNumber = phoneNumberEditText.text.toString().trim()

        if (eventName.isEmpty() || phoneNumber.isEmpty()) {
            showToast("Please fill in all the fields")
            return
        }
        val selectedDate = getSelectedDate()
        if (selectedDate == null) {
            showToast("Please select a date")
            return
        }

        insertSchedule(eventName,phoneNumber,selectedDate,imageUri.toString())
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
                showToast("Data submitted successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error inserting schedule", e)
                showToast("Failed to submit data")
            }
        }
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
                showToast("Image selected: $imageUri")
            }
        }
    }

}