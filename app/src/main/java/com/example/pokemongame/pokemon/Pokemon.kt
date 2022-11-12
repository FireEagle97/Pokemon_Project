package com.example.pokemongame.pokemon

import android.content.Context
import com.example.pokemongame.JSONReader

data class Pokemon(var level: Int,
                   val species: String ,
                   val name : String = species
){
    var baseExperienceReward : Int =0;
    var baseStateAttack : Int = 0;
    var baseStatDefense : Int = 0;
    var baseStatMaxHp : Int = 0;
    var baseStatSpecialAttack : Int = 0;
    var baseStatSpecialDefense : Int =0;
    var baseStatSpeed : Int = 0;
    var types : Array<String> = Array(2){ "" };

}

/**
 * search if we can move PokemonAssinger method to Pokemon class
 *
 */
