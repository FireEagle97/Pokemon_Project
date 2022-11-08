package com.example.pokemongame

import android.content.Context
import java.io.IOException
import java.net.FileNameMap

class JSONReader {
    //Read file to JSON format
    fun JSONReader(context: Context, fileName: String): String? {
        val jsonString:String
        try{
            jsonString = context.assets.open(fileName).bufferedReader().use {
                it.readText()
            }
        } catch (ioException: IOException){
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}