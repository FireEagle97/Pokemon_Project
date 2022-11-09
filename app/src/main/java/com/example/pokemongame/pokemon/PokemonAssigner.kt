package com.example.pokemongame.pokemon

import com.example.pokemongame.JSONReader
import android.content.Context
import java.util.logging.Logger

class PokemonAssigner {

    companion object{
        val PokemonLog : Logger = Logger.getLogger(PokemonAssigner::class.java.name)
    }
    //fun parse json
    fun setAttributes(species: String, context : Context): String? {
        val fileList = context.assets.list("pokemon")!!
        if("$species.json" in fileList) {
            val fileName = "pokemon/$species.json"
            return JSONReader().jSONReader(context, fileName)
            //getting the stats
//            val gson = Gson()
//            val listStates = object: TypeToken<List<State>>() {}.type
//            val statesList : List<State> = gson.fromJson(jsonString,listStates)
//            statesList.forEachIndexed{index, state -> PokemonLog.info("$index:\n$state")}
        }
        return "error"

//        var gson = Gson();
//        Resources.
//        val a = gson.fromJson(Resources., JsonObject::class.java)
//        a.asInt("")
//        a.asInt
//        var jsonString = gson.fromJson<>()

    }


    //assign each field to its coressponding value in the json file

}