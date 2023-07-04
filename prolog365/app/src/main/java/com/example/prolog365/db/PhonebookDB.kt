package com.example.prolog365.db

import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.prolog365.ui.phonebook.PhonebookData

class PhonebookDB {
    companion object {
        val phonebookList = ArrayList<PhonebookData>()
        fun formatPhonenumber(phonenumber: String): String{
            var number = phonenumber.trim()
            if(number.length==11){
                number = number.substring(0, 3) + '-' + number.substring(3, 7) + '-' + number.substring(7)
            }
            return number
        }
        fun findPhonebookDataByName(_name: String): PhonebookData?{
            for(phonebookData in phonebookList){
                if(phonebookData.name==_name){
                    return phonebookData
                }
            }
            return null
        }

        fun findPhonebookDataByNumber(_phoneNumber: String): PhonebookData?{
            for(phonebookData in phonebookList){
                if(phonebookData.phonenumber == _phoneNumber){
                    return phonebookData
                }
            }
            return null
        }
        fun getPhoneNumbers(fragment: Fragment, sort:String, searchName:String?){
            phonebookList.clear()
            Log.d("MyLog", "getPhoneNumbers")
            val addressUri = ContactsContract.Contacts.CONTENT_URI
            val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

            //Log.d("Basic Syntax", sort + searchName)
            val projections = arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                , ContactsContract.CommonDataKinds.Phone.NUMBER)

            var wheneClause:String? = null
            var whereValues:Array<String>? = null
            if(searchName?.isNotEmpty() == true){
                wheneClause = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "like ?"
                whereValues = arrayOf("%$searchName")
            }
            val optionSort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " $sort"

            val cursor = fragment.activity?.contentResolver?.query(phoneUri, projections, wheneClause, whereValues, optionSort)

            while(cursor?.moveToNext()?:false){
                val name = cursor?.getString(1)
                var number = cursor?.getString(2)
                if (name!=null && number != null) {
                    number = formatPhonenumber(number)
                    val phonebookData = PhonebookData(name, number)
                    phonebookList.add(phonebookData)
                }
            }
        }
    }
}