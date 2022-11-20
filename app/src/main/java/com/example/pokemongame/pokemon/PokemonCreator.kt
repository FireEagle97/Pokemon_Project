package com.example.pokemongame.pokemon

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException


class PokemonCreator {

    private fun getPokemonBattleStats(species: String, context: Context): BattleStats {
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

    fun createPokemon(level: Int, species: String, name: String = species, context: Context): Pokemon {
        //get The battleStats
        val battleStats: BattleStats = getPokemonBattleStats(species, context)
        val pokemonTypes: List<String> = battleStats.types
        val moves: MutableList<Move> = mutableListOf()
        //temp vals
        val experience: Int = 0
        val hp: Int = battleStats.baseStatMaxHp

        return Pokemon(battleStats, species, name, moves, experience, level, pokemonTypes, hp)


    }
}
