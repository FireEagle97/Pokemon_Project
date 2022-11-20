package com.example.pokemongame

import android.content.Context
import com.example.pokemongame.pokemon.BattleStats
import com.example.pokemongame.pokemon.BattleStatsGetter
import com.google.gson.Gson
import java.io.IOException

//class JSONReader {
//    //Read file to JSON format
//    fun jSONReader(context: Context, fileName: String): BattleStats? {
//        val jsonString:String
//        return try{
//
//            var gson = Gson();
//            var data = gson.fromJson(jsonString,BattleStats::class.java )
//    //            data.species
//            return data
//
//        } catch (ioException: IOException){
//            ioException.printStackTrace()
//            null
//        }
//
//    }
//}
