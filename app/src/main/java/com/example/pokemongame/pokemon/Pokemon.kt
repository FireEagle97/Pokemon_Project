package com.example.pokemongame.pokemon

//experience type is temporary an int
data class Pokemon(
    val battleStats: BattleStats,
    var experienceReward: Int,
    var attack: Int,
    var defense: Int,
    var maxHp: Int,
    var specialAttack: Int,
    var specialDefense: Int,
    var speed: Int,
    var name: String,
    val moves: MutableList<Move>,
    val experience: Int,
    val level: Int,
    var hp: Int

)

