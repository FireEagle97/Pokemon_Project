package com.example.pokemongame.pokemon

data class Pokemon(
    val species: String,
    var experienceReward: Int,
    var attack: Int,
    var defense: Int,
    var maxHp: Int,
    var specialAttack: Int,
    var specialDefense: Int,
    var speed: Int,
    var types: List<String>,
    var name: String = species,
    val moves: MutableList<Move>,
    val experience: Int,
    val level: Int,
    val hp: Int

)
