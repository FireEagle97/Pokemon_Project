package com.example.pokemongame.pokemon

data class Move(
    val accuracy: Int, val maxPP: Int, val pp: Int = maxPP,
    val power: Int, val damageClass: String, val type: String,
    val target: String, val ailment: String? = null, val ailmentChance: Int) {}
