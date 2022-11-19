package com.example.pokemongame.battle

import android.content.Context
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

    //Performs a whole battle turn
    fun battleTurn(pokemon1: Pokemon, pokemon1Move: Move, pokemon2: Pokemon, pokemon2Move: Move, context: Context){
        val firstPokemon = speedCheck(pokemon1, pokemon2)
        if(firstPokemon == pokemon1){
            doMove(pokemon1, pokemon1Move, pokemon2, context)
        } else {
            doMove(pokemon2, pokemon2Move, pokemon1, context)
        }
        //Check if the opposing pokemon has fainted
            //Death check here
        //If the other pokemon is still alive, it also takes its turn
        if(firstPokemon == pokemon1){
            doMove(pokemon2, pokemon2Move, pokemon1, context)
        } else {
            doMove(pokemon1, pokemon1Move, pokemon2, context)
        }
            //Death check here
    }

    //Determines who should play first based on Speed
    private fun speedCheck(pokemon1: Pokemon, pokemon2: Pokemon): Pokemon{
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
    private fun doMove(attackingPokemon: Pokemon, attackerMove: Move, defendingPokemon: Pokemon, context: Context){
        if(accuracyCheck(attackerMove)){
            //Check if the move does damage
            if(attackerMove.power > 0){
                DamageCalculations().calculateDamage(attackingPokemon, attackerMove, defendingPokemon, context);
            } else if(attackerMove.heal > 0){
                //healing code
                attackingPokemon.HP += attackerMove.heal
                BattleLog.info("${attackingPokemon.name} healed itself!")
            } else {
                BattleLog.info("Moves that have no power or no heal values do nothing")
            }
            //Add stuff here
        } else{
            BattleLog.info("${attackingPokemon.name}'s ${attackerMove.name} missed!")
        }
    }

    //checks if a move should hit
    private fun accuracyCheck(move: Move): Boolean{
        val threshold = random.nextInt(101)
        return move.accuracy > threshold
    }
}