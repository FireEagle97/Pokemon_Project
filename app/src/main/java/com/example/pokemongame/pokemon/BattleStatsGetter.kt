package com.example.pokemongame.pokemon

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.util.logging.Logger

class BattleStatsGetter {

    companion object{
        val BattleStatsLog : Logger = Logger.getLogger(BattleStats::class.java.name)
    }

    fun getPokemonBattleStats(species: String, context: Context): BattleStats {
        val fileList = context.assets.list("pokemon")!!
        var battleStatsData: String = ""
        try {
        if ("${species}.json" in fileList) {
            val fileName = "pokemon/${species}.json"

            battleStatsData = context.assets.open(fileName).bufferedReader().use {
                it.readText()
            }

        }
        }catch (ioException: IOException) {
                ioException.printStackTrace()
                null
            }
        val gson = Gson()
        val listBattleStatsType = object : TypeToken<BattleStats>() {}.type

        return gson.fromJson(battleStatsData, listBattleStatsType)

    }


}