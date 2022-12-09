package com.example.pokemongame.pokemon

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BattleStats(
            @PrimaryKey val species: String,
            var baseExperienceReward : Int,
            var baseStatAttack : Int,
            var baseStatDefense : Int,
            var baseStatMaxHp : Int,
            var baseStatSpecialAttack : Int,
            var baseStatSpecialDefense : Int,
            var baseStatSpeed : Int,
            var types : List<String>,


): java.io.Serializable

