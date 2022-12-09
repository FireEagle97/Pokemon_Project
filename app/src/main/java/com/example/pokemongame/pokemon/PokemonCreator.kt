package com.example.pokemongame.pokemon

import android.content.Context
import android.widget.Toast
import com.example.pokemongame.utility.PokeApiEndpoint
import java.util.logging.Logger
import com.example.pokemongame.utility.Connector
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import java.net.URL

class PokemonCreator {
    companion object{
        val pokemonCreatorLog : Logger = Logger.getLogger(PokemonCreator::class.java.name)
    }


    fun createPokemon(level: Int, species: String, context: Context, name : String = species): Pokemon {
        val db = AppDatabase.getDatabase(context)
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
                if(db.BattleStatsDao().checkIfExists(species)){
                    battleStats = db.BattleStatsDao().getBattleStats(species)
                    Toast.makeText(context, "Loaded battlestats from db", Toast.LENGTH_SHORT).show()
                }
                else {
                    val url = URL("${PokeApiEndpoint.POKEMON.url}/${species}")
                    val data = Connector().connect(url) as String
                    battleStats = PokeAPI().simplifyBattleStats(data)
                    sprites = PokeAPI().getPokemonSprite(data)
                }
            }
            job.join()
            baseExperienceReward  = battleStats!!.baseExperienceReward
            baseStatAttack = battleStats!!.baseStatAttack
            baseStatDefense  = battleStats!!.baseStatDefense
            baseStatMaxHp  = battleStats!!.baseStatMaxHp
            baseStatSpecialAttack = battleStats!!.baseStatSpecialAttack
            baseStatSpecialDefense = battleStats!!.baseStatSpecialDefense
            baseStatSpeed  = battleStats!!.baseStatSpeed

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
            species,
            sprites["front_sprite"].asString,
            sprites["back_sprite"].asString,
        )
    }
}
