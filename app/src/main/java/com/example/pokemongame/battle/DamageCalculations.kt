package com.example.pokemongame.battle

import com.example.pokemongame.pokemon.Move
import com.example.pokemongame.pokemon.MoveAssigner
import com.example.pokemongame.pokemon.Pokemon
import java.util.logging.Logger

class DamageCalculations {
    companion object{
        val DamageLog: Logger = Logger.getLogger(DamageCalculations::class.java.name)
    }
    //If the move does no damage, determine it outside of this class
    fun attack(attackerPokemon: Pokemon, attackerMove : Move, defenderPokemon: Pokemon){

        //Attack hits Defence and Special Attack hits Special Defence
        var damage: Int = if(attackerMove.damageClass == "PHYSICAL"){
            calculateDamage(attackerPokemon.level, attackerPokemon.Attack, attackerMove, defenderPokemon.Defence)
        } else {
            calculateDamage(attackerPokemon.level, attackerPokemon.SpecialAttack, attackerMove, defenderPokemon.SpecialDefence)
        }

        //Calculate STAB bonus
        if(attackerMove.type in attackerPokemon.types){

        }
    }

    private fun calculateDamage(attackerLevel: Int, attackerStat: Int, attackerMove: Move, defenderStat: Int): Int {
        val damage: Int = (((2 * attackerLevel)/5 + 2) / 50) * attackerMove.power * (attackerStat/defenderStat) + 2
        return damage
    }
}