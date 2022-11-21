package com.example.pokemongame.pokemon

data class Pokemon(
    var battleStats: BattleStats,
    var experienceReward: Int,
    var attack: Int,
    var defense: Int,
    var maxHp: Int,
    var specialAttack: Int,
    var specialDefense: Int,
    var speed: Int,
    var name: String,
    var moves: MutableList<Move>,
    var experience: Double,
    var level: Int,
    var hp: Int

)
