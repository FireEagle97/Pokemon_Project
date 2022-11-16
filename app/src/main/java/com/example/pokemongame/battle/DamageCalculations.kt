package com.example.pokemongame.battle

import android.content.Context
import com.example.pokemongame.JSONReader
import com.example.pokemongame.pokemon.Move
import com.example.pokemongame.pokemon.MoveAssigner
import com.example.pokemongame.pokemon.Pokemon
import com.google.gson.Gson
import java.util.logging.Logger

class DamageCalculations {
    companion object{
        val DamageLog: Logger = Logger.getLogger(DamageCalculations::class.java.name)
    }
    //If the move does no damage, determine it outside of this class
    fun calculateDamage(attackerPokemon: Pokemon, attackerMove : Move, defenderPokemon: Pokemon, context: Context): Int{

        //Attack hits Defence and Special Attack hits Special Defence
        var damage: Int = if(attackerMove.damageClass == "PHYSICAL"){
            calculateInitialDamage(attackerPokemon.level, attackerPokemon.Attack, attackerMove, defenderPokemon.Defence)
        } else {
            calculateInitialDamage(attackerPokemon.level, attackerPokemon.SpecialAttack, attackerMove, defenderPokemon.SpecialDefence)
        }

        //Calculate STAB bonus
        if(attackerMove.type in attackerPokemon.types){
            DamageLog.info("STAB bonus applied!")
            damage = kotlin.math.floor(damage * 1.5).toInt()
        }

        //Type effectiveness
        var multiplier: Double = 1.0
        val gson = Gson()
        var effectivenessJsonString = JSONReader().jSONReader(context, "type_relations/${attackerMove.type}.json")
        var effectiveness = gson.fromJson(effectivenessJsonString, Effectiveness::class.java)
        defenderPokemon.types.forEach {
            if(effectiveness == null){

            }
        }

        return damage
    }

    private fun calculateInitialDamage(attackerLevel: Int, attackerStat: Int, attackerMove: Move, defenderStat: Int): Int {
        val damage: Int = (((2 * attackerLevel)/5 + 2) / 50) * attackerMove.power * (attackerStat/defenderStat) + 2
        return damage
    }
}