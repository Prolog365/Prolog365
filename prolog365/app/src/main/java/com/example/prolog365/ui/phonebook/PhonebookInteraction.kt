package com.example.prolog365.ui.phonebook

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity


class PhonebookInteraction {
    companion object {
        fun getFirstLetter(word: String): String{
            val unicodeValue = word[0].toInt() - 0xAC00
            val jongseongIndex = unicodeValue % 28
            val jungseongIndex = (unicodeValue / 28) % 21
            val choseongIndex = (unicodeValue / 28) / 21

            val choseongLetter = ('ᄀ'.code + choseongIndex).toChar()
            val jungseongLetter = ('ᅡ'.code + jungseongIndex).toChar()
            val jongseongLetter = when (jongseongIndex) {
                0 -> ""
                else -> ('ᆧ'.code + jongseongIndex).toChar().toString()
            }

            return "$choseongLetter$jungseongLetter$jongseongLetter"
        }

        fun sendMessage(phonenumber: String, context: Context) {
            val messageUri = Uri.parse("smsto:$phonenumber")
            val callIntent = Intent(Intent.ACTION_SENDTO, messageUri)
            startActivity(context, callIntent, null)
        }

        fun sendCall(phonenumber: String, context: Context){
            val phonecallUri = Uri.parse("tel:$phonenumber")
            val callIntent = Intent(Intent.ACTION_CALL, phonecallUri)
            startActivity(context, callIntent, null)
        }
    }
}