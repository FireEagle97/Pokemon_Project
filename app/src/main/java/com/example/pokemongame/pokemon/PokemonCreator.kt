package com.example.pokemongame.pokemon

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.util.logging.Logger


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

    fun createPokemon(level: Int, species: String, name: String = species, context: Context): Pokemon {
        //get The battleStats
        val battleStats: BattleStats = getPokemonBattleStats(species, context)
        val pokemonTypes: List<String> = battleStats.types
        val moves: MutableList<Move> = mutableListOf()
        //temp vals
        val experience: Int = 0
        val hp: Int = battleStats.baseStatMaxHp
        val baseExperienceReward : Int = battleStats.baseExperienceReward
        val baseStateAttack : Int = battleStats.baseStateAttack
        val baseStatDefense : Int = battleStats.baseStatDefense
        val baseStatMaxHp : Int = battleStats.baseStatMaxHp
        val baseStatSpecialAttack : Int = battleStats.baseStatSpecialAttack
        val baseStatSpecialDefense : Int = battleStats.baseStatSpecialDefense
        val baseStatSpeed : Int = battleStats.baseStatSpeed
        val types : List<String> = battleStats.types
        return Pokemon(
                species,
                baseExperienceReward,
                baseStateAttack,
                baseStatDefense,
                baseStatMaxHp,
                baseStatSpecialAttack,
                baseStatSpecialDefense,
                baseStatSpeed,
                types,
                name,
                moves,
                experience,
                level,
                hp
        )
    }
}
