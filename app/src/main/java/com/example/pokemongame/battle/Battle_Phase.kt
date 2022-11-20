package com.example.pokemongame.battle

import android.content.Context
import com.example.pokemongame.pokemon.Level
import com.example.pokemongame.pokemon.Move
import com.example.pokemongame.pokemon.Pokemon
import java.util.Random
import java.util.logging.Logger

class Battle_Phase(val playerTeam: MutableList<Pokemon>, val enemyTeam: MutableList<Pokemon>) {
    companion object{
        val BattleLog: Logger = Logger.getLogger(Battle_Phase::class.java.name)
    }
    private val random: Random = Random()

    //Performs a whole battle turn when the fight button is pressed
    fun battleTurn(pokemonPlayer: PokemonInTeam, pokemonEnemy: PokemonInTeam, context: Context){
        //List to hold pokemon about to faint
        var faintList = mutableListOf<PokemonInTeam>()
        //List to keep the speed order
        var speedArray: Array<PokemonInTeam> = speedCheck(pokemonPlayer, pokemonEnemy)

        //Call doMove
        doMove(speedArray, true, context)

        //Update the teams
        updateTeam(pokemonPlayer, pokemonEnemy, speedArray)

        //Check if the opposing pokemon has fainted
        faintCheck(speedArray, faintList)
        //If it did, call onFaint
        if(faintList.isNotEmpty()){
            onFaint(speedArray, faintList)
            faintList.clear()
        }

        //If the other pokemon is still alive, it also takes its turn (order doesn't matter here)
        doMove(speedArray, false, context)

        //Update the teams
        updateTeam(pokemonPlayer, pokemonEnemy, speedArray)

        //Check if the opposing pokemon has fainted
        faintCheck(speedArray, faintList)
        //If it did, call onFaint
        if(faintList.isNotEmpty()){
            onFaint(speedArray, faintList)
            faintList.clear()
        }
    }

    //Determines who should play first based on Speed
    private fun speedCheck(pokemon1: PokemonInTeam, pokemon2: PokemonInTeam): Array<PokemonInTeam>{
        val array: Array<PokemonInTeam> = arrayOf<PokemonInTeam>(pokemon1, pokemon2)

        //Speed Tie, roll random
        if (pokemon1.pokemon.Speed == pokemon2.pokemon.Speed){
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
        return if(pokemon1.pokemon.Speed > pokemon2.pokemon.Speed){
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
    private fun doMove(speedArray: Array<PokemonInTeam>, firstTurn: Boolean, context: Context) {
        //If its the second turn, switch their positions
        if(!firstTurn){
            val temp = speedArray[0]
            speedArray[0] = speedArray[1]
            speedArray[1] = temp
        }

        //Check if move hits
        if(accuracyCheck(speedArray[0].chosenMove)){

            //Check if the move does damage
            if(speedArray[0].chosenMove.power > 0){
                speedArray[1].pokemon.HP -= DamageCalculations().calculateDamage(speedArray[0].pokemon, speedArray[0].chosenMove, speedArray[1].pokemon, context)
                //Set HP to 0 if it would bring it into the negatives instead
                if(speedArray[1].HP < 0){
                    speedArray[1].HP = 0
                }

            //If the move heals, it heals
            } else if(speedArray[1].chosenMove.heal > 0){
                speedArray[0].HP += speedArray[1].chosenMove.heal
                BattleLog.info("${speedArray[0].pokemon.name} healed itself!")

            } else {
                BattleLog.info("Moves that have no power or no heal values do nothing")
            }
        } else{
            BattleLog.info("${speedArray[0].pokemon.name}'s ${speedArray[1].chosenMove.name} missed!")
        }
    }

    //Checks if a move should hit
    private fun accuracyCheck(move: Move): Boolean{
        val threshold = random.nextInt(101)
        return move.accuracy > threshold
    }

    //checks if the enemy pokemon is dead
    private fun faintCheck(pokemonArray: Array<PokemonInTeam>, list: MutableList<PokemonInTeam>){
        pokemonArray.forEach { pokemonInTeam ->
            if(pokemonInTeam.pokemon.HP <= 0){
                list.add(pokemonInTeam)
            }
        }
    }

    private fun updateTeam(pokemonPlayer: PokemonInTeam, pokemonEnemy: PokemonInTeam, speedArray: Array<PokemonInTeam>){
        if(speedArray[0].inPlayerTeam) {
            playerTeam[pokemonPlayer.indexInTeam] = speedArray[0].pokemon
        } else {
            playerTeam[pokemonPlayer.indexInTeam] = speedArray[1].pokemon
        }
        if(speedArray[1].inPlayerTeam) {
            enemyTeam[pokemonEnemy.indexInTeam] = speedArray[0].pokemon
        } else {
            enemyTeam[pokemonEnemy.indexInTeam] = speedArray[1].pokemon
        }
    }

    private fun onFaint(array: Array<PokemonInTeam>, list: MutableList<PokemonInTeam>){
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