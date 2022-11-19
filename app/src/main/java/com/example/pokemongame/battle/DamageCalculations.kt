package com.example.pokemongame.battle

import android.content.Context
import com.example.pokemongame.JSONReader
import com.example.pokemongame.pokemon.Move
import com.example.pokemongame.pokemon.MoveAssigner
import com.example.pokemongame.pokemon.Pokemon
import com.google.gson.Gson
import java.util.logging.Logger
import kotlin.math.floor

class DamageCalculations {
    companion object{
        val DamageLog: Logger = Logger.getLogger(DamageCalculations::class.java.name)
    }
    //Note: If the move does no damage because it isn't a damaging move, determine it outside of this class
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
            damage = floor(damage * 1.5).toInt()
        }

        //Type effectiveness
        val multiplier : Double = calculateTypeEffectiveness(attackerMove, defenderPokemon, context)
        damage = floor(damage * multiplier).toInt()

        return damage
    }

    private fun calculateTypeEffectiveness(attackerMove: Move, defenderPokemon: Pokemon, context: Context): Double{
        var multiplier: Double = 1.0
        val gson = Gson()
        var effectivenessJsonString = JSONReader().jSONReader(context, "type_relations/${attackerMove.type}.json")
        var effectivenessRelations = gson.fromJson(effectivenessJsonString, Map::class.java)
        //For each type, modify the multiplier
        defenderPokemon.types.forEach {
            if(effectivenessRelations[it] != null){
                when(effectivenessRelations[it]){
                    "no_effect" -> multiplier = 0.0
                    "not_very_effective" -> multiplier /= 2
                    "super_effective" -> multiplier *= 2
                }
            }
        }
        //Log a battle message
        when(multiplier){
            0.0 -> DamageLog.info("It had no effect...")
            0.25 -> DamageLog.info("It's barely effective...")
            0.50 -> DamageLog.info("It's barely effective...")
            2.0 -> DamageLog.info("It's super effective!")
            4.0 -> DamageLog.info("It's hyper effective!")
        }
        return multiplier
    }

    private fun calculateInitialDamage(attackerLevel: Int, attackerStat: Int, attackerMove: Move, defenderStat: Int): Int {
        val damage: Int = (((2 * attackerLevel)/5 + 2) / 50) * attackerMove.power * (attackerStat/defenderStat) + 2
        return damage
    }
}