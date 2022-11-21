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
    //temp code to reset pp and hp to its max value
    fun resetPokemons(context: Context) : List<Pokemon>{
        val pokemon1 = createPokemon(2,"bulbasaur", context)
        val pokemon2 = createPokemon(1,"charmander", context)
        val pokemon3 = createPokemon(3, "chamander", context)
        val pokemonList : List<Pokemon> = listOf(pokemon1,pokemon2,pokemon3)
        for (pokemon in pokemonList) {
            pokemon.hp = pokemon.maxHp
            pokemon.moves.forEachIndexed{ index, move -> move.pp = move.maxPP}
        }
        return pokemonList
    }
    //temp code to return a list of random Pokemon
    //will use it to generate the opponent team
    fun generateOpponentTeam(context: Context): List<Pokemon>{

    }

    fun createPokemon(level: Int, species: String, context: Context,name : String = species): Pokemon {
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
