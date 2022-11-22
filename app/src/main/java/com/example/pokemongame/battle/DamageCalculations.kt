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
        var damage: Double = if(attackerMove.damageClass == "PHYSICAL"){
            calculateInitialDamage(attackerPokemon.level, attackerPokemon.attack, attackerMove, defenderPokemon.defense)
        } else {
            calculateInitialDamage(attackerPokemon.level, attackerPokemon.specialAttack, attackerMove, defenderPokemon.specialDefense)
        }

        //Calculate STAB bonus
        if(attackerMove.type in attackerPokemon.battleStats.types){
            DamageLog.info("STAB bonus applied!")
            damage *= 1.5
        }

        //Type effectiveness
        var multiplier: Double = 1.0
        val gson = Gson()
        var effectivenessJsonString = JSONReader().jSONReader(context, "type_relations/${attackerMove.type}.json")
        var effectivenessRelations = gson.fromJson(effectivenessJsonString, Map::class.java)
        defenderPokemon.battleStats.types.forEach {
            if(effectivenessRelations[it] != null){
                when(effectivenessRelations[it]){
                    "no_effect" -> multiplier = 0.0
                    "not_very_effective" -> multiplier /= 2
                    "super_effective" -> multiplier *= 2
                }
            }
        }
        damage = floor(damage * multiplier)

        return damage.toInt()
    }

    private fun calculateInitialDamage(attackerLevel: Int, attackerStat: Int, attackerMove: Move, defenderStat: Int): Double {
        val damage: Double = ((((2 * attackerLevel.toDouble())/5) + 2) / 50) * attackerMove.power.toDouble() * (attackerStat.toDouble()/defenderStat.toDouble()) + 2
        DamageLog.info("Initial Damage: $damage")
        return damage
    }
}