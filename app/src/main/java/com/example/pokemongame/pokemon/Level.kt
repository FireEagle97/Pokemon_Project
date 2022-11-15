package com.example.pokemongame.pokemon

import android.content.Context
import kotlin.math.floor
import com.example.pokemongame.pokemon.MoveAssigner
import java.util.logging.Logger

class Level {
    companion object{
        val LevelLog: Logger = Logger.getLogger(MoveAssigner::class.java.name)
    }

    fun addExperience(pokemon: Pokemon, experience: Double, context: Context){
        pokemon.experience += experience
        LevelLog.info("${pokemon.name} gained $experience experience points!")
        checkForLevelUp(pokemon, context)
    }

    private fun checkForLevelUp(pokemon: Pokemon, context: Context){
        val levelFormula = floor(Math.cbrt(pokemon.experience))
        if(pokemon.level < levelFormula){
            levelUp(pokemon, context)
        }
    }

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