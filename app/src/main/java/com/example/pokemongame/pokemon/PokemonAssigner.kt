package com.example.pokemongame.pokemon

import com.example.pokemongame.JSONReader
import android.content.Context
import java.util.logging.Logger

class PokemonAssigner {

    companion object{
        val PokemonLog : Logger = Logger.getLogger(PokemonAssigner::class.java.name)
    }
    //fun parse json
    fun createPokemon(pokemon : Pokemon, context : Context): Pokemon {

        when (pokemon.species) {
            "bulbasaur" -> {
                setPokemonAttributes(pokemon, context);
            }
            "charmander" ->{
                setPokemonAttributes(pokemon, context)
            }
            "squirtle" -> {
                setPokemonAttributes(pokemon, context)
            }
            "pidgey" -> {
                setPokemonAttributes(pokemon, context)
            }
            "rattata" -> {
                setPokemonAttributes(pokemon, context)
            }

        }

    return pokemon;
    }
    private fun setPokemonAttributes(pokemon : Pokemon, context: Context) : Pokemon{
        val fileList = context.assets.list("pokemon")!!
        if("${pokemon.species}.json" in fileList) {
            val fileName = "pokemon/${pokemon.species}.json"
            var data = JSONReader().jSONReader(context, fileName)
            if(data != null){
                pokemon.baseExperienceReward = data.baseExperienceReward
                pokemon.baseStatDefense = data.baseStatDefense
                pokemon.baseStateAttack = data.baseStateAttack
                pokemon.baseStatMaxHp = data.baseStatMaxHp
                pokemon.baseStatSpecialAttack = data.baseStatSpecialAttack
                pokemon.baseStatSpecialDefense = data.baseStatSpecialDefense
                pokemon.baseStatSpeed = data.baseStatSpeed
                pokemon.types = data.types
            }
        }
        return pokemon;

    }


    //assign each field to its coressponding value in the json file

}