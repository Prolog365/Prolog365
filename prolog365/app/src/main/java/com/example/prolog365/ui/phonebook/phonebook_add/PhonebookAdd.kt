package com.example.prolog365.ui.phonebook.phonebook_add

import android.content.ContentProviderOperation
import android.content.ContentProviderResult
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.ContactsContract
import android.view.Gravity
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.example.prolog365.databinding.AddPhonebookBinding
import com.example.prolog365.db.PhonebookDB
import com.example.prolog365.ui.phonebook.PhonebookFragment
import com.example.prolog365.ui.phonebook.phonebook_info.PhonebookInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.invoke.ConstantCallSite

class PhonebookAdd {



    companion object{
        var _addbinding: AddPhonebookBinding? = null
        var popupWindow: PopupWindow? = null
        private val binding get() = _addbinding!!
        private var context: Context? = null
        private var fragment: PhonebookFragment? = null

        fun checkNumberText(): String{
            var numberText = binding.phonenumberTextAddPhonebook.text.toString();
            numberText = PhonebookDB.formatPhonenumber(numberText)
            var isRightPhonenumber = true
            if(numberText.length!=13){
                isRightPhonenumber = false
            }
            for (index in numberText.indices){
                val char = numberText[index]
                if(index == 3 || index == 8){
                    if(char!='-'){
                        isRightPhonenumber = false
                    }
                }else{
                    if(!char.isDigit()){
                        isRightPhonenumber = false
                    }
                }
            }

            if(isRightPhonenumber){
                return numberText
            }else{
                return ""
            }
        }

        fun addPhonebookInfo(){
            val name = binding.nameTextAddPhonebook.text.toString()
            val phonenumber = checkNumberText()
            if(phonenumber.length==0){
                showToast("전화번호가 올바르지 않습니다")
                return
            }
            /*val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
                // Sets the MIME type to match the Contacts Provider
                type = ContactsContract.RawContacts.CONTENT_TYPE
            }
            intent.apply{
                putExtra(ContactsContract.Intents.Insert.PHONE, phonenumber)
                putExtra(ContactsContract.Intents.Insert.NAME, name)
            }
            context?.let { startActivity(it, intent, null) }*/
            val rawContactUri = context?.contentResolver?.insert(ContactsContract.RawContacts.CONTENT_URI, ContentValues())
            val rawContactId = ContentUris.parseId(rawContactUri ?: return)
            val operations = ArrayList<ContentProviderOperation>()
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build())
            operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phonenumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build())
            val results: Array<ContentProviderResult>
            try {
                results =
                    context?.contentResolver?.applyBatch(ContactsContract.AUTHORITY, operations) as Array<ContentProviderResult>
                showToast("연락처가 추가되었습니다")
                closeWindow()
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle any errors that occurred during the batch operation
                showToast("에러 : 다시 시도하세요")
                return
            }
        }

        fun closeWindow(){
            popupWindow?.dismiss()
            fragment?.reloadPhonebookList()
        }

        fun showPopupWindow(_context: Context, _fragment: PhonebookFragment){
            binding.nameTextAddPhonebook.setText("")
            binding.phonenumberTextAddPhonebook.setText("")
            binding.submitButtonAddPhonebook.setOnClickListener{
                addPhonebookInfo()
            }
            fragment = _fragment
            context = _context
            popupWindow = PopupWindow(binding.root, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            popupWindow!!.isOutsideTouchable = true
            popupWindow!!.isFocusable = true
            popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow!!.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
        }

        private fun showToast(message: String) {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}