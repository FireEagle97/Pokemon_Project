package com.example.pokemongame.pokemon

import android.content.Context
import com.example.pokemongame.JSONReader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import java.util.logging.Logger

class MoveAssigner {
    companion object{
        val MoveLog: Logger = Logger.getLogger(MoveAssigner::class.java.name)
    }
    //All logs can be replaced with more useful stuff like a TextField
    //Assigns a new move to a pokemon if a new move can be learned and if the user chooses so
    fun assignNewMoves(pokemon: Pokemon, level: Int, context: Context){
        val pokemonSpecies = pokemon.species

        //get the file list from the move_lists folder
        val fileList = context.assets.list("move_lists")!!

        //Check if the file exists. If it does, get its name and proceed. If it doesn't, give an error message
        if("$pokemonSpecies.json" in fileList){
            val fileName = "move_lists/$pokemonSpecies.json"

            //Get jsonString with JSONReader
            val moveLevelJsonString = JSONReader().jSONReader(context, fileName)

            val gson = Gson()
            //val listMoveType = object: TypeToken<List<Move>>() {}.type
            //val moveList: List<Move> = gson.fromJson(jsonString, listMoveType)
            val listMoveType = object: TypeToken<List<MoveLevel>>() {}.type
            val moveList: List<MoveLevel> = gson.fromJson(moveLevelJsonString, listMoveType)

            //Log the list for testing
            //moveList.forEachIndexed{index, move -> MoveLog.info("$index:\n$move")}

            //List of string to hold the moves we want
            val newMovesList = mutableListOf<Move>()

            //Check if pokemon has no moves
            if(pokemon.moves.isNullOrEmpty()){
                MoveLog.info("Initializing moves...")
                //Depending on the level, get all new moves + descending moves until we have 4 or no more moves
                var descendingLevel = level
                if(newMovesList.count() < 4) {
                    for (i in level downTo 0) {
                        for (element in moveList) {
                            if (descendingLevel == element.level) {
                                getNewMoves(element.move, newMovesList, gson, context)
                            }
                        }
                        descendingLevel--
                    }
                }
                //log testing
                //newMovesList.forEach{move -> MoveLog.info(move.name)}

                //Insert the moves in the pokemon
                newMovesList.forEach{ move ->
                    pokemon.moves.add(move)
                    MoveLog.info(pokemon.species + " has learned " + move.name)
                }
            } else {

                //Check if pokemon has new moves to learn
                //boolean to flip if a new move is learned
                var noNewMoves = true
                for (moveEntry in moveList) {
                    if (moveEntry.level == level) {

                        //Flip the boolean
                        noNewMoves = false

                        //Get all new moves
                        getNewMoves(moveEntry.move, newMovesList, gson, context)

                        //Replace a move if pokemon already has 4 moves
                        if (pokemon.moves.count() == 4) {
                            MoveLog.info("Pokemon already has 4 moves, which move would you like to replace?\n" +
                                    "1. ${pokemon.moves[0].name}\n" +
                                    "2. ${pokemon.moves[1].name}\n" +
                                    "3. ${pokemon.moves[2].name}\n" +
                                    "4. ${pokemon.moves[3].name}\n")
                            //testing purposes. Should be replaced here by an app section
                            val userInput = "1"
                            val input = userInput.toInt()
                            if(input in 1..4){
                                val oldMove = pokemon.moves[input-1].name
                                pokemon.moves[input-1] = newMovesList[0]
                                MoveLog.info("$oldMove has been replaced by ${pokemon.moves[input-1].name}")
                                newMovesList.removeFirst()
                            }

                        } else {
                            //Learn new move
                            pokemon.moves.add(newMovesList[0])
                            MoveLog.info(pokemon.species + " has learned " + newMovesList[0].name)
                            newMovesList.removeFirst()
                        }
                    }
                }
                //If there are no new moves to be learned, print this message
                if(noNewMoves) {
                    MoveLog.info("$pokemonSpecies doesn't learn a new move at level $level")
                }
            }
        } else {
            MoveLog.info("Pokemon data doesn't exist or isn't accessible!")
        }
    }

    private fun getNewMoves(moveName: String, list: MutableList<Move>, gson: Gson, context: Context) {
        var moveJsonString = JSONReader().jSONReader(context, "moves/$moveName.json")
        var move = gson.fromJson(moveJsonString, Move::class.java)
        list.add(move)
    }

}