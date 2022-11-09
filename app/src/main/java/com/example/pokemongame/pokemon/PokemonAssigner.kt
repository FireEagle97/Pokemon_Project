package com.example.pokemongame.pokemon

import com.example.pokemongame.JSONReader
import android.content.Context
import com.google.gson.Gson

class PokemonAssigner {


    //fun parse json
    fun setAttributes(species: String, context : Context){
        val fileList = context.assets.list("pokemon")!!
        if("$species.json" in fileList){
            val fileName = "pokemon/$species.json"
            val jsonString = JSONReader().jSONReader(context,fileName)
            val gson = Gson()

        }

//        var gson = Gson();
//        Resources.
//        val a = gson.fromJson(Resources., JsonObject::class.java)
//        a.asInt("")
//        a.asInt
//        var jsonString = gson.fromJson<>()

    }


    //assign each field to its coressponding value in the json file

}