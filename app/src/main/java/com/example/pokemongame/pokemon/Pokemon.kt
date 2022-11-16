package com.example.pokemongame.pokemon

data class Pokemon(var name: String, var species: String, val types: MutableList<String>,val moves: MutableList<Move>, var level: Int, var experience: Double,
var MaxHP: Int, var Attack: Int, var Defence: Int, var SpecialAttack: Int, var SpecialDefence: Int, var Speed: Int) {
}