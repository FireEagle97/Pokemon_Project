package com.example.pokemongame.battle

import com.example.pokemongame.pokemon.Move
import com.example.pokemongame.pokemon.Pokemon

//This is used when we select an option when battling. We must transform any pokemon that participate in the turn into this object.
data class ActivePokemon(val pokemon: Pokemon, var chosenMove: Move?, val indexInTeam: Int,
                         val inPlayerTeam: Boolean, val willSwitch: Boolean, val willCapture: Boolean,
                         val willUsePotion: Boolean)