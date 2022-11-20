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

    //Performs a whole battle turn when the fight button is pressed
    fun battleTurn(pokemon1: Pokemon, pokemon1Move: Move, pokemon1Index: Int, pokemon2: Pokemon, pokemon2Move: Move, pokemon2Index: Int, context: Context){
        //List to hold pokemon about to faint
        var faintList = mutableListOf<Pokemon>()
        //List to keep the speed order
        var speedArray: Array<Pokemon> = speedCheck(pokemon1, pokemon2)

        //Call doMove
        speedArray = doMove(speedArray[0], pokemon1Move, speedArray[1], true, context)

        //Update the teams
            //Code to update the teams

        //Check if the opposing pokemon has fainted
        faintList = faintCheck(speedArray, faintList)
        //If it did, call onFaint
        if(faintList.isNotEmpty()){
            onFaint(speedArray, faintList)
            faintList.clear()
        }

        //If the other pokemon is still alive, it also takes its turn
        speedArray = doMove(speedArray[1], pokemon2Move, speedArray[0], false, context)

        //Update the teams
            //Code to update the teams

        //Check if the opposing pokemon has fainted
        faintList = faintCheck(speedArray, faintList)
        if(faintList.isNotEmpty()){
            onFaint(speedArray, faintList)
            faintList.clear()
        }
    }

    //Determines who should play first based on Speed
    private fun speedCheck(pokemon1: Pokemon, pokemon2: Pokemon): Array<Pokemon>{
        val array: Array<Pokemon> = arrayOf<Pokemon>(pokemon1, pokemon2)

        //Speed Tie, roll random
        if (pokemon1.Speed == pokemon2.Speed){
            BattleLog.info("Speed tie occurred!")
            return if(random.nextBoolean()){
                array[0] = pokemon1
                array[1] = pokemon2
                array
            } else {
                array[0] = pokemon2
                array[1] = pokemon1
                array
            }
        }

        //Determine who is faster
        return if(pokemon1.Speed > pokemon2.Speed){
            array[0] = pokemon1
            array[1] = pokemon2
            array
        } else {
            array[0] = pokemon2
            array[1] = pokemon1
            array
        }
    }

    //Calls accuracyCheck and, upon a hit, enact move repercussions
    private fun doMove(attackingPokemon: Pokemon, attackerMove: Move, defendingPokemon: Pokemon, firstTurn: Boolean, context: Context): Array<Pokemon>{
        //Check if move hits
        if(accuracyCheck(attackerMove)){

            //Check if the move does damage
            if(attackerMove.power > 0){
                defendingPokemon.HP -= DamageCalculations().calculateDamage(attackingPokemon, attackerMove, defendingPokemon, context)
                //Set HP to 0 if it would bring it into the negatives instead
                if(defendingPokemon.HP < 0){
                    defendingPokemon.HP = 0
                }

            //If the move heals, it heals
            } else if(attackerMove.heal > 0){
                attackingPokemon.HP += attackerMove.heal
                BattleLog.info("${attackingPokemon.name} healed itself!")

            } else {
                BattleLog.info("Moves that have no power or no heal values do nothing")
            }
        } else{
            BattleLog.info("${attackingPokemon.name}'s ${attackerMove.name} missed!")
        }

        //Arrange the array in its original order and send the updated pokemon back
        if(firstTurn){
            return arrayOf(attackingPokemon, defendingPokemon)
        }
        return arrayOf(defendingPokemon, attackingPokemon)
    }

    //Checks if a move should hit
    private fun accuracyCheck(move: Move): Boolean{
        val threshold = random.nextInt(101)
        return move.accuracy > threshold
    }

    //checks if the enemy pokemon is dead
    private fun faintCheck(pokemonArray: Array<Pokemon>, list: MutableList<Pokemon>): MutableList<Pokemon>{
        pokemonArray.forEach { pokemon ->
            if(pokemon.HP <= 0){
                list.add(pokemon)
            }
        }
        return list
    }

    private fun onFaint(array: Array<Pokemon>, list: MutableList<Pokemon>){
        //If both pokemon are dead, switch both
        if(list.count() == 2){
            //endOfMatchCondition
            //Switch both
        }

        //Find fainted pokemon
            //Find out which pokemon has fainted
            for(listPokemon in list){
                for(pokemon in array){

                }
            }

        //Give exp to victor if alive
            //Give exp
        BattleLog.info("A pokemon has fainted!")
        //Switch pokemons if possible, else declare victory
            //If no other pokemons can be switched for opponent, declare victory
                //declare victory and return to Main Menu
            //If no other pokemons can be switched for player, declare loss
                //declare loss and return to Main Menu
        //code to switch pokemons
    }
}