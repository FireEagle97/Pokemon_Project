package com.example.pokemongame.pokemon

data class Move(
    val accuracy: Int, val pp: Int, val maxPp: Int,
    val power: Int, val damageClass: String, val type: String,
    val target: String, val ailment: String? = null, val ailmentChance: String? = null) {}