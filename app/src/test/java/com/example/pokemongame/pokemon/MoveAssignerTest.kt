package com.example.pokemongame.pokemon

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

internal class MoveAssignerTest {

    @Test
    //At level 5, squirtle learns 2 moves. We check if he has 2 moves
    fun assignNewMoves() {
        val squirtle = PokemonCreator().createPokemon(5, "squirtle")
        MoveAssigner().assignNewMoves(squirtle, squirtle.level)
        assertEquals(2, squirtle.moves.count())
    }
}