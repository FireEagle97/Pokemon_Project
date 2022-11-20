package com.example.pokemongame.pokemon

//experience type is temporary an int
data class Pokemon(
                    val battleStats: BattleStats,
                    val species : String,
                    val name : String,
                    val moves: MutableList<Move>,
                    val experience : Int,
                    val level : Int,
                    val types : List<String>,
                    val hp : Int

)

