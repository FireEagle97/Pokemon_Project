package com.example.pokemongame.pokemon

data class BattleStats(
            val species: String,
            var baseExperienceReward : Int,
            var baseStateAttack : Int,
            var baseStatDefense : Int,
            var baseStateMaxHp : Int,
            var baseStatSpecialAttack : Int,
            var baseStatSpecialDefense : Int,
            var baseStatSpeed : Int,
            var types : List<String>,

): java.io.Serializable

