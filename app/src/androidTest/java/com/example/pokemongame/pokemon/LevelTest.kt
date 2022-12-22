package com.example.pokemongame.pokemon

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.jupiter.api.Assertions.*

import org.junit.Test

internal class LevelTest {

    var instrumentationContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun initializeLevels() {
        val bulbasaur = PokemonCreator().createPokemon(13, "bulbasaur")
        Level().initializeLevels(bulbasaur, bulbasaur.level, instrumentationContext)
        var hasVineWhip = false
        for(move in bulbasaur.moves){
            if(move.name == "vine-whip"){
                hasVineWhip = true
            }
        }
        assertEquals(true, hasVineWhip)
    }

    @Test
    fun addExperience() {
        val bulbasaur = PokemonCreator().createPokemon(13, "bulbasaur")
        Level().initializeLevels(bulbasaur, bulbasaur.level, instrumentationContext)
        val initExperience = bulbasaur.experience
        Level().addExperience(bulbasaur, 200.0,instrumentationContext)
        val addedExperience = bulbasaur.experience
        assertEquals(initExperience+200.0, addedExperience)
    }

    @Test
    fun levelUp() {
        val bulbasaur = PokemonCreator().createPokemon(13, "bulbasaur")
        Level().initializeLevels(bulbasaur, bulbasaur.level, instrumentationContext)
        Level().levelUp(bulbasaur, instrumentationContext)
        assertEquals(14, bulbasaur.level)
    }
}