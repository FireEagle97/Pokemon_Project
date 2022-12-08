package com.example.pokemongame.pokemon

data class BattleStats(
            val species: String,
            var base_exp_reward : Int,
            var base_attack : Int,
            var base_defense : Int,
            var base_maxHp : Int,
            var base_special_attack : Int,
            var base_special_defense : Int,
            var base_speed : Int,
            var types : List<String>,


): java.io.Serializable

