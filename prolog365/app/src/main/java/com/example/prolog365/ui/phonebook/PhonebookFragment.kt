package com.example.prolog365.ui.phonebook

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prolog365.R
import com.example.prolog365.databinding.FragmentPhonebookBinding
import com.example.prolog365.databinding.InfoPhonebookBinding


class PhonebookFragment : Fragment() {

    private var _binding: FragmentPhonebookBinding? = null
    private var _infobinding: InfoPhonebookBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val phonebookList = ArrayList<PhonebookData>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPhonebookBinding.inflate(inflater, container, false)
        _infobinding = InfoPhonebookBinding.inflate(inflater, container, false)
        val root: View = binding.root
        getPhonebookData()
        setRecyclerViewPhonebook()
        return root
    }

    fun setRecyclerViewPhonebook(){
        val adapter = PhonebookAdapter(phonebookList)
        binding.recyclerviewPhonebook.adapter = adapter
        binding.recyclerviewPhonebook.layoutManager = LinearLayoutManager(activity)

        adapter.itemClick = object : PhonebookAdapter.ItemClick{
            override fun onClick(view: View, position: Int){
                // object click event
                clickItemPhonebook(phonebookList[position])
            }
        }
    }

    fun makeInfoData(phonebookData: PhonebookData){
        _infobinding?.nameTextInfoPhonebook?.text = phonebookData.name
        _infobinding?.phonenumberTextInfoPhonebook?.text = phonebookData.phonenumber
    }


    fun showPopupWindow(){
        val popupWindow = PopupWindow(_infobinding?.root, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.showAtLocation(_infobinding?.root, Gravity.CENTER, 0, 0)
    }

    fun clickItemPhonebook(phonebookData: PhonebookData){
        //Log.d("MyLog", "Item Click")
        makeInfoData(phonebookData)
        showPopupWindow()
    }

    val permissions = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE)
    var searchText = ""
    var sortText = "asc"

    @RequiresApi(Build.VERSION_CODES.M)
    fun isPermitted():Boolean{
        for(perm in permissions){
            if(getActivity()?.let { checkSelfPermission(it, perm) } != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }



    fun startProcess(){
        getPhoneNumbers(sortText, searchText)
        //setSearchListener()
        setRecyclerViewPhonebook()
    }


    fun getPhoneNumbers(sort:String, searchName:String?){
        phonebookList.clear()
        Log.d("MyLog", "getPhoneNumbers")
        val addressUri = ContactsContract.Contacts.CONTENT_URI
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        //Log.d("Basic Syntax", sort + searchName)
        val projections = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID
            , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            , ContactsContract.CommonDataKinds.Phone.NUMBER)

        var wheneClause:String? = null
        var whereValues:Array<String>? = null
        if(searchName?.isNotEmpty() == true){
            wheneClause = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "like ?"
            whereValues = arrayOf("%$searchName")
        }
        val optionSort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " $sort"

        val cursor = activity?.contentResolver?.query(phoneUri, projections, wheneClause, whereValues, optionSort)

        while(cursor?.moveToNext()?:false){
            val name = cursor?.getString(1)
            var number = cursor?.getString(2)
            if (name!=null && number != null) {
                if(number.length==11){
                    number = number.substring(0, 3) + '-' + number.substring(3, 7) + '-' + number.substring(7)
                }
                val phonebookData = PhonebookData(name, number)
                phonebookList.add(phonebookData)
            }
        }


    }


    private fun getPhonebookData(){
        if(isLower23() || isPermitted()){
            Log.d("MyLog", "Permission Get")
            startProcess()
        }else{
            Log.d("MyLog", "Permission No")
            //requestPermissions(permissions)
            //ActivityCompat.requestPermissions(requireActivity(), permissions, 99)
            val contract = ActivityResultContracts.RequestMultiplePermissions()
            val activityResultLauncher = registerForActivityResult(contract) { resultMap ->
                val isAllGranted = permissions.all {e -> resultMap[e] == true}
                if(isAllGranted){
                    startProcess()
                }else{
                    Toast.makeText(activity, "권한 승인이 필요합니다", Toast.LENGTH_LONG).show()
                    activity?.finish()
                }
            }

            activityResultLauncher.launch(permissions)
        }
    }

    fun isLower23() : Boolean{
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
    }
/*
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d("MyLog", "Request Permission Result")
        if (requestCode == 99) {
            var check = true
            for (grant in grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    check = false
                    break
                }
            }
            if (check) startProcess()
            else {

            }
        }
    }
 */


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}