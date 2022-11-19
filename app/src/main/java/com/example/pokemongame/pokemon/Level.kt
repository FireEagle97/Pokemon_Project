package com.example.pokemongame.pokemon

import android.content.Context
import kotlin.math.floor
import com.example.pokemongame.pokemon.MoveAssigner
import java.util.logging.Logger
import kotlin.math.pow

class Level {
    companion object{
        val LevelLog: Logger = Logger.getLogger(Level::class.java.name)
    }

    //When creating a new pokemon with a provided level, perform all steps that require that pokemon's
    //stats to augment and for it to learn new moves. We set the pokemon's level to 0 and progressively
    //level it up
    fun initializeLevels(pokemon: Pokemon, level: Int, context: Context){
        pokemon.level = 0;
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
    private fun levelUp(pokemon: Pokemon, context: Context){
        pokemon.level++
        LevelLog.info("${pokemon.name} has leveled up!")

        //Stat changes (MaxHP has a different formula than the others)
        val newMaxHPStat = floor((((pokemon.MaxHP + 10) * pokemon.level) / 50).toDouble()) + pokemon.level + 10
        LevelLog.info("${pokemon.name}'s MaxHP stat has increased to $newMaxHPStat")
        pokemon.MaxHP = newMaxHPStat.toInt()
        val newAttackStat = floor((((pokemon.Attack + 10) * pokemon.level) / 50).toDouble()) + 5
        LevelLog.info("${pokemon.name}'s Attack stat has increased to $newAttackStat")
        pokemon.Attack = newAttackStat.toInt()
        val newDefenceStat = floor((((pokemon.Defence + 10) * pokemon.level) / 50).toDouble()) + 5
        LevelLog.info("${pokemon.name}'s Defence stat has increased to $newDefenceStat")
        pokemon.Defence = newDefenceStat.toInt()
        val newSpecialAttackStat = floor((((pokemon.SpecialAttack + 10) * pokemon.level) / 50).toDouble()) + 5
        LevelLog.info("${pokemon.name}'s Special Attack stat has increased to $newSpecialAttackStat")
        pokemon.SpecialAttack = newSpecialAttackStat.toInt()
        val newSpecialDefenceStat = floor((((pokemon.SpecialDefence + 10) * pokemon.level) / 50).toDouble()) + 5
        LevelLog.info("${pokemon.name}'s Special Defence stat has increased to $newSpecialDefenceStat")
        pokemon.SpecialDefence = newSpecialDefenceStat.toInt()
        val newSpeedStat = floor((((pokemon.Speed + 10) * pokemon.level) / 50).toDouble()) + 5
        LevelLog.info("${pokemon.name}'s Speed stat has increased to $newSpeedStat")
        pokemon.Speed = newSpeedStat.toInt()

        //Assign new moves (if any)
        MoveAssigner().assignNewMoves(pokemon, pokemon.level, context)

        //Evolution code (not needed for milestone 1)
    }
}