package com.example.pokemongame.pokemon

//experience type is temporary an int
data class Pokemon(
                    val species: String,
                    var baseExperienceReward : Int,
                    var baseStateAttack : Int,
                    var baseStatDefense : Int,
                    var baseStatMaxHp : Int,
                    var baseStatSpecialAttack : Int,
                    var baseStatSpecialDefense : Int,
                    var baseStatSpeed : Int,
                    var types : List<String>,
                    val name : String,
                    val moves: MutableList<Move>,
                    val experience : Int,
                    val level : Int,
                    val hp : Int

)

