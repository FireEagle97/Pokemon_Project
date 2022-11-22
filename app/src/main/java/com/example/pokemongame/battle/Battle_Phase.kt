package com.example.pokemongame.battle

import android.content.Context
import com.example.pokemongame.pokemon.Level
import com.example.pokemongame.pokemon.Move
import com.example.pokemongame.pokemon.Pokemon
import java.util.Random
import java.util.logging.Logger

class Battle_Phase(val playerTeam: ArrayList<Pokemon>, val enemyTeam: ArrayList<Pokemon>) {
    companion object{
        val BattleLog: Logger = Logger.getLogger(Battle_Phase::class.java.name)
    }
    private val random: Random = Random()

    //Performs a whole turn when any button is pressed. This returns a boolean that tells the external code when to
    //exit upon victory or loss so that it can go back to the main menu. (Run button logic is handled outside of this class though)
    fun playBattlePhase(pokemonPlayer: ActivePokemon, pokemonEnemy: ActivePokemon, inTrainerBattle: Boolean, context: Context): Array<Boolean>{
        //List to keep the speed order
        var speedArray: Array<ActivePokemon> = speedCheck(pokemonPlayer, pokemonEnemy)

        //array to hold booleans that dictate if the battle should end [0] or if the opposing pokemon has fainted [1]
        // or if the fainted pokemon is in the player's team [2]
        //I would put this in two values instead, but when they are function parameters they are transformed
        //into val, which makes it impossible to change them
        var faintedAndEndBattleArray = arrayOf(false, false, false)

        //First turn
        playSingularTurn(speedArray, true, inTrainerBattle, pokemonPlayer.indexInTeam,
            pokemonEnemy.indexInTeam, faintedAndEndBattleArray, context)
        //If the opposing pokemon has fainted, skip its turn, else perform its turn
        if(faintedAndEndBattleArray[1]){
            //Check if the battle ended, if so, do not log this message
            if(!faintedAndEndBattleArray[0]) {
                BattleLog.info("A pokemon has fainted before it could take its turn. Skipping turn...")
            }
        } else {
            //Second turn
            playSingularTurn(speedArray, false, inTrainerBattle, pokemonPlayer.indexInTeam, pokemonEnemy.indexInTeam, faintedAndEndBattleArray, context)
        }

        return faintedAndEndBattleArray
    }

    //Plays a single trainer's turn
    private fun playSingularTurn(speedArray: Array<ActivePokemon>, firstTurn: Boolean, inTrainerBattle: Boolean,
                                 pokemonPlayerIndex: Int, pokemonEnemyIndex: Int,
                                 faintedAndEndBattleArray: Array<Boolean>, context: Context){
        var index = if (firstTurn){
            0
        } else {
            1
        }

        //Call doMove if the chosenMove isn't null (because of a switch or item use)
        if(speedArray[index].chosenMove != null) {
            doMove(speedArray, firstTurn, context)

            //Update the teams
            updateTeam(pokemonPlayerIndex, pokemonEnemyIndex, speedArray)

            //Check if the opposing pokemon has fainted
            //If it did, call onFaint
            if(faintCheck(speedArray, firstTurn)){
                faintedAndEndBattleArray[1] = true
                onFaint(speedArray, firstTurn, inTrainerBattle, pokemonPlayerIndex, pokemonEnemyIndex,
                    faintedAndEndBattleArray, context)
            }
        } else {
            BattleLog.info("Trainer chose another action than fighting. Turn will be skipped")
        }
    }

    //Determines who should play first based on Speed
    private fun speedCheck(pokemon1: ActivePokemon, pokemon2: ActivePokemon): Array<ActivePokemon>{
        val array: Array<ActivePokemon> = arrayOf<ActivePokemon>(pokemon1, pokemon2)

        //Speed Tie, roll random
        if (pokemon1.pokemon.speed == pokemon2.pokemon.speed){
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
        return if(pokemon1.pokemon.speed > pokemon2.pokemon.speed){
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
        swapArrayPositionsIfSecondTurn(speedArray, firstTurn)

        //Reduce pp of move
        speedArray[0].chosenMove!!.pp--

        //Check if move hits
        if(accuracyCheck(speedArray[0].chosenMove!!)){
            BattleLog.info("${speedArray[0].pokemon.name} used ${speedArray[0].chosenMove!!.name}!")

            //Check if the move does damage
            BattleLog.info("Old HP of defending pokemon: ${speedArray[1].pokemon.hp}")
            if(speedArray[0].chosenMove!!.power > 0){
                speedArray[1].pokemon.hp -= DamageCalculations().calculateDamage(speedArray[0].pokemon,
                    speedArray[0].chosenMove!!, speedArray[1].pokemon, context)
                //Set HP to 0 if it would bring it into the negatives instead
                if(speedArray[1].pokemon.hp < 0){
                    speedArray[1].pokemon.hp = 0
                }
                BattleLog.info("New HP of defending pokemon: ${speedArray[1].pokemon.hp}")

            //If the move heals, it heals
            } else if(speedArray[0].chosenMove!!.heal > 0){
                speedArray[0].pokemon.hp += speedArray[0].chosenMove!!.heal
                //Set HP to maxHP if the healing would bring the hp beyond it
                if(speedArray[0].pokemon.hp > speedArray[0].pokemon.maxHp){
                    speedArray[0].pokemon.hp = speedArray[0].pokemon.maxHp
                }
                BattleLog.info("${speedArray[0].pokemon.name} healed itself!")
                BattleLog.info("New HP of healed pokemon: ${speedArray[0].pokemon.hp}")

            //If it doesn't do either, it does nothing
            } else {
                BattleLog.info("Moves that have no power or no heal values do nothing")
            }
        } else{
            BattleLog.info("${speedArray[0].pokemon.name}'s ${speedArray[0].chosenMove!!.name} missed!")
        }
        //If its the second turn, switch their positions again to keep the original order
        swapArrayPositionsIfSecondTurn(speedArray, firstTurn)
    }

    //Checks if a move should hit
    private fun accuracyCheck(move: Move): Boolean{
        val threshold = random.nextInt(101)
        return move.accuracy > threshold
    }

    //checks if the enemy pokemon is dead
    private fun faintCheck(speedArray: Array<ActivePokemon>, firstTurn: Boolean): Boolean {
        swapArrayPositionsIfSecondTurn(speedArray, firstTurn)
        //If we properly take care of the array order, the opposing pokemon will always be in [1]
        if(speedArray[1].pokemon.hp == 0) {
            swapArrayPositionsIfSecondTurn(speedArray, firstTurn)
            return true
        }
        swapArrayPositionsIfSecondTurn(speedArray, firstTurn)
        return false
    }

    //Updates the teams to retain the updated correct values (like hp and exp gained or lost)
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

    //Awards experience, checks if someone won, else forces a switch
    private fun onFaint(speedArray: Array<ActivePokemon>, firstTurn: Boolean, inTrainerBattle: Boolean,
                        pokemonPlayerIndex: Int, pokemonEnemyIndex: Int, faintedAndEndBattleArray: Array<Boolean>, context: Context) {
        swapArrayPositionsIfSecondTurn(speedArray, firstTurn)

        //Fainted pokemon will always be in [1]
        BattleLog.info("${speedArray[1].pokemon.name} has fainted!")

        //Give exp to victor
        var gainedExperience: Double = 0.3 * speedArray[1].pokemon.experienceReward * speedArray[1].pokemon.level
        if(inTrainerBattle){
            BattleLog.info("Double xp applied due to trainer battle!")
            gainedExperience *= 2.0
        }
        Level().addExperience(speedArray[0].pokemon, gainedExperience, context)

        //Update teams due to xp gain
        updateTeam(pokemonPlayerIndex, pokemonEnemyIndex, speedArray)

        //Check if a victory condition is achieved for either party. If not, force a switch through the BattleActivity
        val playerTeamFainted = checkIfTeamAllFainted(playerTeam)
        val enemyTeamFainted = checkIfTeamAllFainted(enemyTeam)
        if(playerTeamFainted || enemyTeamFainted){
            if(enemyTeamFainted){
                BattleLog.info("You won!")
            }
            else if(checkIfTeamAllFainted(playerTeam)) {
                BattleLog.info("You lost...")
            }
            swapArrayPositionsIfSecondTurn(speedArray, firstTurn)
            faintedAndEndBattleArray[0] = true
        } else {
            //Find if fainted pokemon is in player team or not
            faintedAndEndBattleArray[2] = speedArray[1].inPlayerTeam
        }
        swapArrayPositionsIfSecondTurn(speedArray, firstTurn)
    }

    private fun checkIfTeamAllFainted(team: ArrayList<Pokemon>): Boolean{
        var counter = 0
        for(pokemon in team){
            if(pokemon.hp == 0){
                counter++
            }
        }
        if(counter == team.count()){
            return true
        }
        return false
    }

    //Used to switch the positions of the ActivePokemons in the array
    private fun swapArrayPositionsIfSecondTurn(speedArray: Array<ActivePokemon>, firstTurn: Boolean){
        if(!firstTurn) {
            val temp = speedArray[0]
            speedArray[0] = speedArray[1]
            speedArray[1] = temp
        }
    }
}