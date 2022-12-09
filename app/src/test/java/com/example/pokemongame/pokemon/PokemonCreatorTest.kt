package com.example.pokemongame.pokemon

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

internal class PokemonCreatorTest {

    @Test
    fun createPokemon() {
        val squirtle = PokemonCreator().createPokemon(5, "squirtle")
        val initExperience = squirtle.experience
        val expectedBattleStates =BattleStats("squirtle", 0,0, 0, 0, 0, 0, 0, listOf("water"))
        val expectedSquirtle  = Pokemon(expectedBattleStates,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            "squirtle",
            arrayListOf<Move>(),
            0.0,
            5,
            0,
            "squirtle",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-i/red-blue/transparent/7.png",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-i/red-blue/transparent/back/7.png")
        assertEquals(squirtle,expectedSquirtle)

    }
}