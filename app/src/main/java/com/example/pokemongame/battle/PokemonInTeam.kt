package com.example.pokemongame.battle

import com.example.pokemongame.pokemon.Move
import com.example.pokemongame.pokemon.Pokemon

//This is used when we press fight. We must transform the pokemon that participate into this.
//Id is an arbitrary number that is either 0 or 1
data class PokemonInTeam(val pokemon: Pokemon, val chosenMove: Move, val indexInTeam: Int, val order: Int)