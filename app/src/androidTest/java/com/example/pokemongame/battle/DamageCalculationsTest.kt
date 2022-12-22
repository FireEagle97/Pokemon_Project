package com.example.pokemongame.battle

import android.content.Context
import android.content.Intent
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.pokemongame.BattlePhaseActivity
import com.example.pokemongame.pokemon.Level
import com.example.pokemongame.pokemon.Move
import com.example.pokemongame.pokemon.Pokemon
import com.example.pokemongame.pokemon.PokemonCreator
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.Test

internal class DamageCalculationsTest {

    var instrumentationContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun calculateDamage() {
        val intent = Intent(instrumentationContext, BattlePhaseActivity::class.java)

        val squirtle = PokemonCreator().createPokemon(13, "squirtle")
        Level().initializeLevels(squirtle, squirtle.level, instrumentationContext)
        val bulbasaur = PokemonCreator().createPokemon(13, "bulbasaur")
        Level().initializeLevels(bulbasaur, bulbasaur.level, instrumentationContext)

        val playerTeam = arrayListOf<Pokemon>(squirtle, bulbasaur)
        intent.putExtra("collection", playerTeam)
        intent.putExtra("team", playerTeam)
        intent.putExtra("trainerName", "test")

        launchActivity<BattlePhaseActivity>(intent).use { it ->
            lateinit var move: Move
            for (moves in bulbasaur.moves) {
                if (moves.name == "vine-whip") {
                    move = moves
                }
            }
            var damage = 0
            it.onActivity {
                damage = DamageCalculations(it).calculateDamage(bulbasaur, move, squirtle)
            }
            assertEquals(25, damage)
        }
    }
}