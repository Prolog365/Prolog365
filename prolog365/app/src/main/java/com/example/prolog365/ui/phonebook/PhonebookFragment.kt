package com.example.prolog365.ui.phonebook

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prolog365.R
import com.example.prolog365.databinding.AddPhonebookBinding
import com.example.prolog365.databinding.FragmentPhonebookBinding
import com.example.prolog365.databinding.InfoPhonebookBinding
import com.example.prolog365.db.PhonebookDB
import com.example.prolog365.db.ScheduleDB
import com.example.prolog365.ui.phonebook.phonebook_add.PhonebookAdd
import com.example.prolog365.ui.phonebook.phonebook_info.PhonebookInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate


class PhonebookFragment : Fragment() {

    private var _binding: FragmentPhonebookBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //val phonebookList = ArrayList<PhonebookData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setHasOptionsMenu(true)
        _binding = FragmentPhonebookBinding.inflate(inflater, container, false)
        PhonebookAdd._addbinding = AddPhonebookBinding.inflate(inflater, null, false)
        PhonebookInfo._infobinding = InfoPhonebookBinding.inflate(inflater, container, false)
        val root: View = binding.root
        checkPermission()
        setRecyclerViewPhonebook()
        return root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_phonebook, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_phonebook -> {
                // Handle the "add button" selection
                addPhonebook()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    public fun reloadPhonebookList(){
        PhonebookDB.getPhoneNumbers(this, sortText, searchText)
        setRecyclerViewPhonebook()
    }
    fun addPhonebook(){
        Log.d("MyLog", "Add Phonebook")
        activity?.let { PhonebookAdd.showPopupWindow(it, this) }
    }

    fun setRecyclerViewPhonebook(){
        val adapter = PhonebookAdapter(PhonebookDB.phonebookList)
        binding.recyclerviewPhonebook.adapter = adapter
        binding.recyclerviewPhonebook.layoutManager = LinearLayoutManager(activity)

        adapter.itemClick = object : PhonebookAdapter.ItemClick{
            override fun onClick(view: View, position: Int){
                // object click event
                clickItemPhonebook(PhonebookDB.phonebookList[position])
            }
        }
    }






    fun clickItemPhonebook(phonebookData: PhonebookData){
        //Log.d("MyLog", "Item Click")
        activity?.let { PhonebookInfo.showPopupWindow(phonebookData, it) }
        //PhonebookInfo.showPopupWindow()
    }

    val permissions = arrayOf(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS)
    var searchText = ""
    var sortText = "asc"


    fun isPermitted():Boolean{
        for(perm in permissions){
            if(getActivity()?.let { checkSelfPermission(it, perm) } != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }


    fun initDatabase(){
        ScheduleDB.initDB(this)
        // DB TEST
        CoroutineScope(Dispatchers.IO).launch{
            //ScheduleDB.clearDB()
            /*ScheduleDB.insertDB("Schedule1", LocalDate.now(), "010-1234-5678", "drawable/asdf.png")
            ScheduleDB.insertDB("Schedule2", LocalDate.now(), "010-2345-6789", "drawable/bcde.png")
            ScheduleDB.insertDB("Schedule3", LocalDate.now(), "010-1234-5678", "drawable/bcde.png")
            ScheduleDB.insertDB("Schedule4", LocalDate.now(), "010-1234-5678", "drawable/bcde.png")
            ScheduleDB.insertDB("Schedule5", LocalDate.now(), "010-3456-7890", "drawable/bcde.png")
            ScheduleDB.logDB()*/
        }
    }


    fun startProcess(){
        PhonebookDB.getPhoneNumbers(this, sortText, searchText)
        initDatabase()
        //setSearchListener()
        setRecyclerViewPhonebook()
    }

    private fun getPermission(){
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
    private fun checkPermission(){
        if(isPermitted()){
            //Log.d("MyLog", "Permission Get")
            startProcess()
        }else{
            //Log.d("MyLog", "Permission No")
            getPermission()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}