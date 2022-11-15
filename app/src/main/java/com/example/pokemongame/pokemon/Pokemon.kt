package com.example.pokemongame.pokemon

data class Pokemon(var name: String, var species: String, val moves: MutableList<Move>, var level: Int, var experience: Double) {
}