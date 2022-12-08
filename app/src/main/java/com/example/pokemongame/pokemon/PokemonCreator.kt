package com.example.pokemongame.pokemon

import android.content.Context
import com.example.pokemongame.utility.PokeApiEndpoint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.util.logging.Logger
import androidx.lifecycle.lifecycleScope
import com.example.pokemongame.utility.Connector
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import java.net.URL
import java.util.Random

class PokemonCreator {
    companion object{
        val pokemonCreatorLog : Logger = Logger.getLogger(PokemonCreator::class.java.name)
    }


    fun createPokemon(level: Int, species: String,name : String = species): Pokemon {
        //get The battleStats
        var baseExperienceReward : Int
        var baseStatAttack : Int
        var baseStatDefense : Int
        var baseStatMaxHp : Int
        var baseStatSpecialAttack : Int
        var baseStatSpecialDefense : Int
        var baseStatSpeed : Int
        var battleStats: BattleStats? = null
        val moves: ArrayList<Move> = arrayListOf()
        var sprites  = JsonObject()
        val experience = 0.0
        val hp = 0
        runBlocking {
            val scope = CoroutineScope(Dispatchers.IO)
            val job = scope.launch {
                val url = URL("${PokeApiEndpoint.POKEMON.url}/${species}")
                val data = Connector().connect(url) as String
                battleStats = PokeAPI().simplifyBattleStats(data)
                sprites = PokeAPI().getPokemonSprite(data)
            }
            job.join()
            baseExperienceReward  = battleStats!!.base_exp_reward
            baseStatAttack = battleStats!!.base_attack
            baseStatDefense  = battleStats!!.base_defense
            baseStatMaxHp  = battleStats!!.base_maxHp
            baseStatSpecialAttack = battleStats!!.base_special_attack
            baseStatSpecialDefense = battleStats!!.base_special_defense
            baseStatSpeed  = battleStats!!.base_speed

        }
        return Pokemon(
            battleStats!!,
            baseExperienceReward,
            baseStatAttack,
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
            sprites["front_sprite"].asString
        )
    }
}
