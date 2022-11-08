package com.example.pokemongame.pokemon

import android.content.Context
import com.example.pokemongame.JSONReader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.logging.Logger

class MoveAssigner {
    companion object{
        val MoveLog: Logger = Logger.getLogger(MoveAssigner::class.java.name)
    }
    //All logs can be replaced with more useful stuff like a TextField
    //Assigns a new move to a pokemon if a new move can be learned and if the uses chooses so
    fun assignNewMoves(pokemon: Pokemon, level: Int, context: Context){
        val pokemonSpecies = pokemon.species

        //get the file list from the move_lists folder
        val fileList = context.assets.list("move_lists")!!

        //Check if the file exists. If it does, get its name and proceed. If it doesn't, give an error message
        if("$pokemonSpecies.json" in fileList){
            val fileName = "move_lists/$pokemonSpecies.json"

            //Get jsonString with JSONReader
            val jsonString = JSONReader().jSONReader(context, fileName)

            val gson = Gson()
            //val listMoveType = object: TypeToken<List<Move>>() {}.type
            //val moveList: List<Move> = gson.fromJson(jsonString, listMoveType)
            val listMoveType = object: TypeToken<List<MoveLevel>>() {}.type
            val moveList: List<MoveLevel> = gson.fromJson(jsonString, listMoveType)

            //Log the list for testing
            moveList.forEachIndexed{index, move -> MoveLog.info("$index:\n$move")}

            //Check if pokemon has no moves
            if(pokemon.moves.isNullOrEmpty()){
                MoveLog.info("Learn first new moves here")
                //Depending on the level, get all new moves + descending moves until we have 4
            }

            //Check if pokemon has new moves to learn
            for(moveEntry in moveList){
                if(moveEntry.level == level){

                    //Get all new moves

                    //Check if pokemon already has moves
                    if(pokemon.moves!!.size == 4) {
                        //Ask for move replacement
                        MoveLog.info("Which move would you like to replace?")
                    } else {
                        //Learn new move here
                        MoveLog.info(pokemon.species+" has learned "+"move here")
                    }
                } else {
                    MoveLog.info("$pokemonSpecies doesn't learn a new move on level $level")
                }
            }
        } else {
            MoveLog.info("Pokemon data doesn't exist or isn't accessible!")
        }
    }
}