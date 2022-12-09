package com.example.pokemongame.battle

import android.content.Context
import com.example.pokemongame.BattlePhaseActivity
import com.example.pokemongame.pokemon.Move
import com.example.pokemongame.pokemon.Pokemon
import com.example.pokemongame.utility.Connector
import com.example.pokemongame.utility.PokeApiEndpoint
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import simplifyTypeRelations
import java.net.URL
import java.util.logging.Logger
import kotlin.math.floor

class DamageCalculations(val activity: BattlePhaseActivity) {
    companion object{
        val DamageLog: Logger = Logger.getLogger(DamageCalculations::class.java.name)
    }
    //Note: If the move does no damage because it isn't a damaging move, determine it outside of this class
    fun calculateDamage(attackerPokemon: Pokemon, attackerMove : Move, defenderPokemon: Pokemon): Int{

        //Attack hits Defence and Special Attack hits Special Defence
        var damage: Double = if(attackerMove.damageClass == "PHYSICAL"){
            calculateInitialDamage(attackerPokemon.level, attackerPokemon.attack, attackerMove, defenderPokemon.defense)
        } else {
            calculateInitialDamage(attackerPokemon.level, attackerPokemon.specialAttack, attackerMove, defenderPokemon.specialDefense)
        }

        //Calculate STAB bonus
        if(attackerMove.type in attackerPokemon.battleStats!!.types){
            DamageLog.info("STAB bonus applied!")
            damage *= 1.5
        }

        //Type effectiveness
        val multiplier : Double = calculateTypeEffectiveness(attackerMove, defenderPokemon)
        damage = floor(damage * multiplier)
        DamageLog.info("Total damage: $damage")

        return damage.toInt()
    }

    private fun calculateTypeEffectiveness(attackerMove: Move, defenderPokemon: Pokemon): Double =
        runBlocking {
        var multiplier: Double = 1.0

        val gson = Gson()
        val scope = CoroutineScope(Dispatchers.IO)
            var attackerMoveEffectivenessString: String = ""
        val job = scope.launch {
                //Get the data
                val url: URL = URL("${PokeApiEndpoint.TYPE.url}/${attackerMove.type}")
                var data = Connector().connect(url) as String
                //Simplify it
                attackerMoveEffectivenessString = simplifyTypeRelations(data)
        }
            job.join()

                //Transform it into a map
                val attackerMoveEffectiveness =
                    gson.fromJson(attackerMoveEffectivenessString, Map::class.java)

                //If the defender's type is found in the type relations of the attacking move, perform these operations
                defenderPokemon.battleStats!!.types.forEach {
                    if (attackerMoveEffectiveness[it] != null) {
                        when (attackerMoveEffectiveness[it]) {
                            "no_effect" -> multiplier = 0.0
                            "not_very_effective" -> multiplier /= 2
                            "super_effective" -> multiplier *= 2
                        }
                    }
                }
            //Log a battle message
            var message: String = ""
            when (multiplier) {
                0.0 -> message = "It had no effect..."
                0.25 -> message = "It's barely effective..."
                0.50 -> message = "It's not very effective..."
                1.0 -> message = "It's effective"
                2.0 -> message = "It's super effective!"
                4.0 -> message = "It's hyper effective!"
            }
            activity.addStringToBattleTextList(message)
            return@runBlocking multiplier
    }

    private fun calculateInitialDamage(attackerLevel: Int, attackerStat: Int, attackerMove: Move, defenderStat: Int): Double {
        val damage: Double = ((((2 * attackerLevel.toDouble())/5) + 2) / 50) * attackerMove.power.toDouble() * (attackerStat.toDouble()/defenderStat.toDouble()) + 2
        DamageLog.info("Initial Damage: $damage")
        return damage
    }
}