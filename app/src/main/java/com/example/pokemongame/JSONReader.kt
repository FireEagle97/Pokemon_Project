package com.example.pokemongame

import android.content.Context
import com.example.pokemongame.pokemon.Pokemon
import com.google.gson.Gson
import java.io.IOException

class JSONReader {
    //Read file to JSON format
    fun jSONReader(context: Context, fileName: String): Pokemon? {
        val jsonString:String
        return try{
            jsonString = context.assets.open(fileName).bufferedReader().use {
                it.readText()
            }
            var gson = Gson();
            var data = gson.fromJson(jsonString,Pokemon::class.java )
    //            data.species
            return data

        } catch (ioException: IOException){
            ioException.printStackTrace()
            null
        }

    }
}
