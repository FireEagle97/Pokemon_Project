package com.example.pokemongame.battle

import android.content.Context
import android.hardware.camera2.CameraManager.AvailabilityCallback
import androidx.annotation.ArrayRes
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

    //Performs a whole turn when any button is pressed. Run logic is handled outside of here since it isn't the same as the games
    fun playTurn(pokemonPlayer: ActivePokemon, pokemonEnemy: ActivePokemon, inTrainerBattle: Boolean, context: Context){
        //List to keep the speed order
        var speedArray: Array<ActivePokemon> = speedCheck(pokemonPlayer, pokemonEnemy)

        playSingularTurn(speedArray, true, inTrainerBattle, pokemonPlayer.indexInTeam, pokemonEnemy.indexInTeam, context)
        //Start of second player's turn
        playSingularTurn(speedArray, false, inTrainerBattle, pokemonPlayer.indexInTeam, pokemonEnemy.indexInTeam, context)
    }

    //Plays a single trainer's turn
    private fun playSingularTurn(speedArray: Array<ActivePokemon>, firstTurn: Boolean, inTrainerBattle: Boolean,
                                 pokemonPlayerIndex: Int, pokemonEnemyIndex: Int, context: Context){
        var index = if (firstTurn){
            0
        } else {
            1
        }
        //Check for switch
        if(speedArray[index].willSwitch){
            BattleLog.info("Switch called!")
            //Code to switch here (swap between speedArray and teams above) (also must transform switching out pokemon
            //back into a Pokemon object and switching in into an ActivePokemon with null chosenMove and false booleans)
        }

        //Check for items
        if(speedArray[index].willCapture || speedArray[index].willUsePotion){
            //Code for using items here.
        }

        //Call doMove
        if(speedArray[index].chosenMove != null) {
            doMove(speedArray, firstTurn, context)

            //Update the teams
            updateTeam(pokemonPlayerIndex, pokemonEnemyIndex, speedArray)

            //Check if the opposing pokemon has fainted
            //If it did, call onFaint
            if(faintCheck(speedArray, firstTurn)){
                onFaint(speedArray, firstTurn)
            }
        } else {
            BattleLog.info("Trainer chose another action than fighting. Turn will be skipped")
        }
    }

    //Determines who should play first based on Speed
    private fun speedCheck(pokemon1: ActivePokemon, pokemon2: ActivePokemon): Array<ActivePokemon>{
        val array: Array<ActivePokemon> = arrayOf<ActivePokemon>(pokemon1, pokemon2)

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
    private fun doMove(speedArray: Array<ActivePokemon>, firstTurn: Boolean, context: Context) {
        //If its the second turn, switch their positions
        if(!firstTurn){
            swapArrayPositions(speedArray)
        }

        //Check if move hits
        if(accuracyCheck(speedArray[0].chosenMove!!)){
            BattleLog.info("${speedArray[0].pokemon.name} used ${speedArray[0].chosenMove!!.name}!")

            //Check if the move does damage
            if(speedArray[0].chosenMove!!.power > 0){
                speedArray[1].pokemon.HP -= DamageCalculations().calculateDamage(speedArray[0].pokemon, speedArray[0].chosenMove!!, speedArray[1].pokemon, context)
                //Set HP to 0 if it would bring it into the negatives instead
                if(speedArray[1].HP < 0){
                    speedArray[1].HP = 0
                }
                BattleLog.info("New HP of defending pokemon: ${speedArray[1].pokemon.hp}")

            //If the move heals, it heals
            } else if(speedArray[0].chosenMove!!.heal > 0){
                speedArray[0].HP += speedArray[0].chosenMove!!.heal
                //Set HP to maxHP if the healing would bring the hp beyond it
                if(speedArray[0].pokemon.hp > speedArray[0].pokemon.maxHP){
                    speedArray[0].pokemon.hp = speedArray[0].pokemon.maxHP
                }
                BattleLog.info("${speedArray[0].pokemon.name} healed itself!")
                BattleLog.info("New HP of healed pokemon: ${speedArray[0].pokemon.hp}")

            } else {
                BattleLog.info("Moves that have no power or no heal values do nothing")
            }
        } else{
            BattleLog.info("${speedArray[0].pokemon.name}'s ${speedArray[0].chosenMove!!.name} missed!")
        }
        //If its the second turn, switch their positions again to keep the original order
        if(!firstTurn){
            swapArrayPositions(speedArray)
        }
    }

    //Checks if a move should hit
    private fun accuracyCheck(move: Move): Boolean{
        val threshold = random.nextInt(101)
        return move.accuracy > threshold
    }

    //checks if the enemy pokemon is dead
    private fun faintCheck(speedArray: Array<ActivePokemon>, firstTurn: Boolean): Boolean {
        if(!firstTurn){
            swapArrayPositions(speedArray)
        }
        if(speedArray[1].pokemon.hp == 0) {
            if(!firstTurn){
                swapArrayPositions(speedArray)
            }
            return true
        }
        return false
    }

    //Updates the teams to hold the updated correct values
    private fun updateTeam(pokemonPlayerIndex: Int, pokemonEnemyIndex: Int, speedArray: Array<ActivePokemon>){
        if(speedArray[0].inPlayerTeam) {
            playerTeam[pokemonPlayerIndex] = speedArray[0].pokemon
        } else {
            playerTeam[pokemonPlayerIndex] = speedArray[1].pokemon
        }
        if(speedArray[1].inPlayerTeam) {
            enemyTeam[pokemonEnemyIndex] = speedArray[0].pokemon
        } else {
            enemyTeam[pokemonEnemyIndex] = speedArray[1].pokemon
        }
    }

    //Checks if someone won, else forces a switch
    private fun onFaint(speedArray: Array<ActivePokemon>, firstTurn: Boolean){
        if(!firstTurn){
            swapArrayPositions(speedArray)
        }

        //Fainted pokemon will always be in [1]
        BattleLog.info("${speedArray[1].pokemon.name} has fainted!")
        //Give exp to victor
            //Give exp

        //Switch pokemons if possible, else declare victory
            //If no other pokemons can be switched for opponent, declare victory
                //declare victory and return to Main Menu
            //If no other pokemons can be switched for player, declare loss
                //declare loss and return to Main Menu
        //Code to switch here (swap between speedArray and teams above) (also must transform switching out pokemon
        //back into a Pokemon object and switching in into an ActivePokemon with null chosenMove and false booleans)
        if(!firstTurn){
            swapArrayPositions(speedArray)
        }
    }

    private fun checkEndOfMatchCondition(){

    }

    //Used to switch the positions of the ActivePokemons in the array
    private fun swapArrayPositions(speedArray: Array<ActivePokemon>){
        val temp = speedArray[0]
        speedArray[0] = speedArray[1]
        speedArray[1] = temp
    }
}