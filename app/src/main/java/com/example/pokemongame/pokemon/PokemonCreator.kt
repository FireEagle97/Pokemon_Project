package com.example.pokemongame.pokemon

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.util.logging.Logger
import java.util.Random

class PokemonCreator {
    companion object{
        val pokemonCreatorLog : Logger = Logger.getLogger(PokemonCreator::class.java.name)
    }

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

    fun createPokemon(level: Int, species: String, context: Context,name : String = species): Pokemon {
        //get The battleStats
        val battleStats: BattleStats = getPokemonBattleStats(species, context)
        val moves: MutableList<Move> = mutableListOf()
        //temp vals
        val experience: Double = 0.0
        val hp: Int = 0
        val baseExperienceReward : Int = battleStats.baseExperienceReward
        val baseStateAttack : Int = battleStats.baseStateAttack
        val baseStatDefense : Int = battleStats.baseStatDefense
        val baseStatMaxHp : Int = battleStats.baseStateMaxHp
        val baseStatSpecialAttack : Int = battleStats.baseStatSpecialAttack
        val baseStatSpecialDefense : Int = battleStats.baseStatSpecialDefense
        val baseStatSpeed : Int = battleStats.baseStatSpeed
        return Pokemon(
                battleStats,
                baseExperienceReward,
                baseStateAttack,
                baseStatDefense,
                baseStatMaxHp,
                baseStatSpecialAttack,
                baseStatSpecialDefense,
                baseStatSpeed,
                name,
                moves,
                experience,
                level,
                hp,
        )
    }
}
