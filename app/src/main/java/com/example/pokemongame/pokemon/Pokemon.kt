package com.example.pokemongame.pokemon

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.pokemongame.R

@Entity
data class Pokemon(
    @Ignore var battleStats: BattleStats, //will get it from BattleStats
    var experienceReward: Int,
    var attack: Int,
    var defense: Int,
    var maxHp: Int,
    var specialAttack: Int,
    var specialDefense: Int,
    var speed: Int,
    var name: String,
    var moves: ArrayList<Move>,
    var experience: Double,
    var level: Int,
    var hp: Int,
    var frontUrl: String = "", //WILL NEED TO PUT THOSE HERE
    var backUrl: String = "",
    var inTeam: Boolean = false,
    var ordering: Int = 0 //will be changed when we store it in db, this is what we order by.
): java.io.Serializable{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
//given pokemon species finds its resource files (hard coded)
fun getPokemonImageResourceId(species: String): Int =
    when(species){
        "squirtle" -> R.drawable.squirtle
        "bulbasaur" -> R.drawable.bulbasaur
        "charmander" -> R.drawable.charmander
        else -> R.drawable.question_mark
    }
