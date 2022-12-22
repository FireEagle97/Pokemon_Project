package com.example.pokemongame.pokemon

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import kotlin.math.floor
import java.util.logging.Logger
import kotlin.math.pow

class Level {
    lateinit var moveAssigner: MoveAssigner
    //Boolean to determine if the pokemon came from the player
    var fromPlayer: Boolean = false
    //Boolean to determine if the call to level up came from initializeLevels (to not show all Toasts)
    var cameFromInitializeLevels: Boolean = false
    constructor(){
        moveAssigner = MoveAssigner()
    }
    constructor(fragmentManager: FragmentManager){
        moveAssigner = MoveAssigner(fragmentManager)
        fromPlayer = true
    }
    companion object{
        val LevelLog: Logger = Logger.getLogger(Level::class.java.name)
    }

    //When creating a new pokemon with a provided level, perform all steps that require that pokemon's
    //stats to augment and for it to learn new moves. We set the pokemon's level to 0 and progressively
    //level it up
    fun initializeLevels(pokemon: Pokemon, level: Int, context: Context){
        pokemon.level -= 1
        cameFromInitializeLevels = true
        val totalExperience = level.toDouble().pow(3.toDouble())
        addExperience(pokemon, totalExperience, context)
    }

    //Adds the specified experience to a pokemon and calls checkForLevelUp
    fun addExperience(pokemon: Pokemon, experience: Double, context: Context){
        pokemon.experience += experience
        LevelLog.info("${pokemon.name} gained $experience experience points!")
        checkForLevelUp(pokemon, context)
    }

    //Checks if it is time for a pokemon to level up by calculating the level it should have based on its experience and then
    //calls levelUp a number of times depending on that difference
    private fun checkForLevelUp(pokemon: Pokemon, context: Context){
        val levelFormula: Int = floor(Math.cbrt(pokemon.experience)).toInt()
        if(pokemon.level < levelFormula){
            for(i in 1..(levelFormula-pokemon.level)) {
                levelUp(pokemon, context)
            }
        }
    }

    //levels up a pokemon (raises its level, raises its stats, and calls assignNewMoves())
    fun levelUp(pokemon: Pokemon, context: Context){
        pokemon.level++
        LevelLog.info("${pokemon.name} has leveled up to level ${pokemon.level}!")
        if(fromPlayer && !cameFromInitializeLevels){
            Toast.makeText(context,"${pokemon.name} has leveled up to level ${pokemon.level}!", Toast.LENGTH_SHORT).show()
        }

        //Stat changes (MaxHP has a different formula than the others)
        val newMaxHPStat = floor((((pokemon.battleStats!!.baseStatMaxHp + 10) * pokemon.level) / 50).toDouble()) + pokemon.level + 10
        LevelLog.info("${pokemon.name}'s MaxHP stat has increased to $newMaxHPStat")
        pokemon.maxHp = newMaxHPStat.toInt()
        val newAttackStat = floor((((pokemon.battleStats!!.baseStatAttack + 10) * pokemon.level) / 50).toDouble()) + 5
        LevelLog.info("${pokemon.name}'s Attack stat has increased to $newAttackStat")
        pokemon.attack = newAttackStat.toInt()
        val newDefenceStat = floor((((pokemon.battleStats!!.baseStatDefense + 10) * pokemon.level) / 50).toDouble()) + 5
        LevelLog.info("${pokemon.name}'s Defence stat has increased to $newDefenceStat")
        pokemon.defense = newDefenceStat.toInt()
        val newSpecialAttackStat = floor((((pokemon.battleStats!!.baseStatSpecialAttack + 10) * pokemon.level) / 50).toDouble()) + 5
        LevelLog.info("${pokemon.name}'s Special Attack stat has increased to $newSpecialAttackStat")
        pokemon.specialAttack = newSpecialAttackStat.toInt()
        val newSpecialDefenceStat = floor((((pokemon.battleStats!!.baseStatSpecialDefense + 10) * pokemon.level) / 50).toDouble()) + 5
        LevelLog.info("${pokemon.name}'s Special Defence stat has increased to $newSpecialDefenceStat")
        pokemon.specialDefense = newSpecialDefenceStat.toInt()
        val newSpeedStat = floor((((pokemon.battleStats!!.baseStatSpeed + 10) * pokemon.level) / 50).toDouble()) + 5
        LevelLog.info("${pokemon.name}'s Speed stat has increased to $newSpeedStat")
        pokemon.speed = newSpeedStat.toInt()

        //Assign new moves (if any)
        moveAssigner.assignNewMoves(pokemon, pokemon.level)

        //Reset cameFromInitializeLevels
        cameFromInitializeLevels = false
    }
}