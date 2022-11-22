package com.example.pokemongame.pokemon

import com.example.pokemongame.R

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

): java.io.Serializable
//given pokemon species finds its resource files (hard coded)
fun getPokemonImageResourceId(species: String): Int =
    when(species){
        "squirtle" -> R.drawable.squirtle
        "bulbasaur" -> R.drawable.bulbasaur
        "charmander" -> R.drawable.charmander
        else -> R.drawable.question_mark
    }
