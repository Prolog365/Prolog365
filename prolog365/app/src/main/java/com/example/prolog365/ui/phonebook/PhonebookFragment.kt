package com.example.prolog365.ui.phonebook

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prolog365.databinding.FragmentPhonebookBinding

class PhonebookFragment : Fragment() {

    private var _binding: FragmentPhonebookBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val phonebookList = ArrayList<PhonebookData>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val phonebookViewModel =
            ViewModelProvider(this).get(PhonebookViewModel::class.java)

        _binding = FragmentPhonebookBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //getPhonebookData()

        //val phonebookList =

        phonebookList.add(PhonebookData("가나다", "010-1234-5678"))
        phonebookList.add(PhonebookData("나다라", "010-2345-6789"))
        phonebookList.add(PhonebookData("다라마", "010-3345-6789"))
        phonebookList.add(PhonebookData("라마바", "010-4345-6789"))
        phonebookList.add(PhonebookData("마바사", "010-5345-6789"))
        phonebookList.add(PhonebookData("바사아", "010-6345-6789"))
        phonebookList.add(PhonebookData("사아자", "010-7345-6789"))
        phonebookList.add(PhonebookData("아자차", "010-8345-6789"))
        phonebookList.add(PhonebookData("자차카", "010-9345-6789"))
        phonebookList.add(PhonebookData("차카타", "010-0345-6789"))


        binding.recyclerviewPhonebook.adapter = PhonebookAdapter(phonebookList)
        if (container != null) {
            binding.recyclerviewPhonebook.layoutManager =
                LinearLayoutManager(container.getContext())
        }

        return root
    }
    /*
    val permissions = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE)
    lateinit var adapter:ContactsAdapter
    var list = mutableListOf()
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
        setList()
        setSearchListener()
        setRadioListener()
    }

    fun getPhoneNumbers(sort:String, searchName:String?):List{
        val list = mutableListOf()
        val addressUri = ContactsContract.Contacts.CONTENT_URI
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        val projections = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID
            , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            , ContactsContract.CommonDataKinds.Phone.NUMBER)

        var wheneClause:String? = null
        var whereValues:Array? = null
        if(searchName?.isNotEmpty() ?: false){
            wheneClause = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "like ?"
            whereValues = arrayOf("%$searchName")
        }
    }


    fun setList(){
        list.addAll(getPhoneNumbers(sortText, searchText))
    }



    private fun getPhonebookData(){
        if(isLower23() || isPermitted()){
            startProcess()
        }else{
            getActivity()?.let { ActivityCompat.requestPermissions(it, permissions, 99) }
        }
    }

    fun isLower23() : Boolean{
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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
                Toast.makeText(activity, "권한 승인이 필요합니다", Toast.LENGTH_LONG).show()
                activity?.finish()
            }
        }
    }

     */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}