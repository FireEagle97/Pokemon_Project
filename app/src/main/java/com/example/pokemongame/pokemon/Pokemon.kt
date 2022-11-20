package com.example.pokemongame.pokemon

//experience type is temporary an int
data class Pokemon(
                    val battleStats: BattleStats,
                    val species : String,
                    val name : String = species,
                    val moves: MutableList<Move>,
                    val experience : Int,
                    val level : Int,
                    val types : MutableList<String>,
                    val hp : Int

)

