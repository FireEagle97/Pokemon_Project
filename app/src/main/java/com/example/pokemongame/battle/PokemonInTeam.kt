package com.example.pokemongame.battle

import com.example.pokemongame.pokemon.Move
import com.example.pokemongame.pokemon.Pokemon

//This is used when we press fight. We must transform the pokemon that participate into this.
//Id is an arbitrary number that is either 1 or 2. They must be different
data class PokemonInTeam(val pokemon: Pokemon, val chosenMove: Move, val indexInTeam: Int, val inPlayerTeam: Boolean)