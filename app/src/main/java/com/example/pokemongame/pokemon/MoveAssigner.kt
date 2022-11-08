package com.example.pokemongame.pokemon

import android.content.Context
import android.util.Log
import com.example.pokemongame.JSONReader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.logging.Logger

class MoveAssigner {
    companion object{
        val MoveLog: Logger = Logger.getLogger(MoveAssigner::class.java.name)
        lateinit var appContext: Context
    }
    //All logs can be replaced with more useful stuff like a TextField
    //Assigns a new move to a pokemon if a new move can be learned and if the uses chooses so
    fun AssignNewMoves(pokemon: Pokemon, level: Int){
        //Check if the file exists
        val fileName = ""
                //pokemon.species +".json"
        val file = File(fileName)
        //Boolean value to check if the file exists
        var fileExists = file.exists()
        //If the file exists, proceed, else get a message
        if(fileExists){
            //Gson stuff to get move data
            val jsonString = JSONReader().JSONReader(appContext, fileName)
            val gson = Gson()
            val listMoveType = object: TypeToken<List<Move>>() {}.type
            val moveList: List<Move> = gson.fromJson(jsonString, listMoveType)
            //Log the file for testing
            moveList.forEachIndexed{index, move -> MoveLog.info("$index:\n$move")}

            //Check if pokemon has new moves to learn
            //MoveLog.info("No new moves can be learned")
            //Check if pokemon already has moves
            if(pokemon.moves!!.size == 4) {
                //Ask for move replacement
                MoveLog.info("Which move would you like to replace?")
            } else {
                //Learn move here
                MoveLog.info(pokemon.species+" has learned "+"move here")
            }
        } else {
            MoveLog.info("Pokemon data doesn't exist or isn't accessible!")
        }
    }
}