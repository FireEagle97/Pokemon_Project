package com.example.pokemongame.battle

import com.example.pokemongame.pokemon.Level
import com.example.pokemongame.pokemon.Move
import com.example.pokemongame.pokemon.Pokemon
import java.util.Random
import java.util.logging.Logger

class Battle_Phase {
    companion object{
        val BattleLog: Logger = Logger.getLogger(Battle_Phase::class.java.name)
    }
    private val random: Random = Random()

    //Determines who should play first based on Speed
    fun speedCheck(pokemon1: Pokemon, pokemon2: Pokemon): Pokemon{
        //Speed Tie, roll random
        if (pokemon1.Speed == pokemon2.Speed){
            BattleLog.info("Speed tie occurred!")
            return if(random.nextBoolean()){
                pokemon1
            } else {
                pokemon2
            }
        }
        //Determine who is faster
        return if(pokemon1.Speed > pokemon2.Speed){
            pokemon1
        } else {
            pokemon2
        }
    }

    //Calls accuracyCheck and, upon a hit, enact move repercussions
    fun doMove(pokemon: Pokemon, move: Move){
        if(accuracyCheck(move)){
            //Add stuff here
        } else{
            BattleLog.info("${pokemon.name}'s ${move.name} missed!")
        }
    }

    //checks if a move should hit
    private fun accuracyCheck(move: Move): Boolean{
        val threshold = random.nextInt(101)
        return move.accuracy > threshold
    }
}