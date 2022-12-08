package com.example.pokemongame.pokemon

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.pokemongame.R

//(foreignKeys = [ForeignKey(entity = BattleStats::class, parentColumns = arrayOf("species"), childColumns = arrayOf(
//    "species"
//), onDelete = CASCADE)])
@Entity
data class Pokemon(
    @Ignore var battleStats: BattleStats,
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
    constructor( experienceReward: Int,
                        attack: Int,
                        defense: Int,
                        maxHp: Int,
                        specialAttack: Int,
                        specialDefense: Int,
                        speed: Int,
                        name: String,
                        moves: ArrayList<Move>,
                        experience: Double,
                        level: Int,
                        hp: Int,
                        frontUrl: String = "", //WILL NEED TO PUT THOSE HERE
                        backUrl: String = "",
                        inTeam: Boolean = false,
                        ordering: Int = 0) : this(BattleStats("",0,0,0,0,0,0,0,listOf("")),experienceReward, attack, defense, maxHp, specialAttack, specialDefense, speed, name, moves, experience, level, hp, frontUrl)
}
//given pokemon species finds its resource files (hard coded)
fun getPokemonImageResourceId(species: String): Int =
    when(species){
        "squirtle" -> R.drawable.squirtle
        "bulbasaur" -> R.drawable.bulbasaur
        "charmander" -> R.drawable.charmander
        else -> R.drawable.question_mark
    }
