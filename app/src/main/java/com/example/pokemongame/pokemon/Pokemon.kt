package com.example.pokemongame.pokemon


data class Pokemon(
                    val battleStats: BattleStats,
                    val species : String,
                    val name : String = species,
                    val moves: MutableList<Move>

)

