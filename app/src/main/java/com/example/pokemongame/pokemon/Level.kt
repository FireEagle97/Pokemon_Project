package com.example.pokemongame.pokemon

import android.content.Context
import kotlin.math.floor
import com.example.pokemongame.pokemon.MoveAssigner

class Level {
    fun checkForLevelUp(pokemon: Pokemon, context: Context){
        val levelFormula = floor(Math.cbrt(pokemon.experience))
        if(pokemon.level < levelFormula){
            levelUp(pokemon, context)
        }
    }

    private fun levelUp(pokemon: Pokemon, context: Context){
        pokemon.level++
        MoveAssigner.MoveLog.info("${pokemon.name} has leveled up!")
        //stat changes here
        //Assign new moves to leveled-up pokemon
        MoveAssigner().assignNewMoves(pokemon, pokemon.level, context)
    }
}