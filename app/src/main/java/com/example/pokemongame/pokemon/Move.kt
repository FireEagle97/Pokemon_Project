package com.example.pokemongame.pokemon

data class Move(
    val name: String, val accuracy: Int, val maxPP: Int, var pp: Int,
    val power: Int, val heal: Int, val damageClass: String, val type: String,
    val target: String, val ailment: String? = null, val ailmentChance: Int)

