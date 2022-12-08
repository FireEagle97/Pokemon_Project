package com.example.pokemongame.pokemon

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.pokemongame.AddMoveDialogFragment
import com.example.pokemongame.utility.JSONReader
import com.example.pokemongame.battle.SelectMovesFragment
import com.example.pokemongame.utility.Connector
import com.example.pokemongame.utility.PokeApiEndpoint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import simplifyMove
import simplifyMoves
import java.net.URL
import java.util.logging.Logger
import kotlin.random.Random

class MoveAssigner {
    var fragmentManager : FragmentManager?
    var playerPokemon = false
    constructor(){
        fragmentManager = null
    }
    constructor(fragmentManagerNew: FragmentManager){
        fragmentManager = fragmentManagerNew
        playerPokemon = true
    }
    companion object{
        val MoveLog: Logger = Logger.getLogger(MoveAssigner::class.java.name)
    }

    val random = Random

    //All logs can be replaced with more useful stuff like a TextField
    //Assigns a new move to a pokemon if a new move can be learned and if the user chooses so
    fun assignNewMoves(pokemon: Pokemon, level: Int, context: Context){
        val pokemonSpecies = pokemon.battleStats.species
        val gson = Gson()

        runBlocking {
            val scope = CoroutineScope(Dispatchers.IO)
            var moveList: List<MoveLevel> = mutableListOf()
            val job = scope.launch{
                //Get the data
                val url: URL = URL("${PokeApiEndpoint.POKEMON.url}/${pokemon.battleStats.species}")
                val data = Connector().connect(url) as String
                //Simplify it
                val moveListString = simplifyMoves(data)

                //Transform it into a List of MoveLevels
                val listMoveType = object: TypeToken<List<MoveLevel>>() {}.type
                moveList = gson.fromJson(moveListString, listMoveType)
            }
            job.join()

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
                                getNewMove(element.move, newMovesList, gson, context)
                            }
                        }
                        descendingLevel--
                    }
                }

                //Insert the moves in the pokemon
                newMovesList.forEach{ move ->
                    pokemon.moves.add(move)
                    MoveLog.info(pokemon.name + " has learned " + move.name)
                }
            } else {

                //Check if pokemon has new moves to learn
                //boolean to flip if a new move is learned
                var noNewMoves = true
                for (moveEntry in moveList) {
                    if (moveEntry.level == level) {

                        //Flip the boolean
                        noNewMoves = false

                        //Get a new move
                        getNewMove(moveEntry.move, newMovesList, gson, context)

                        //Ask if the user wants the pokemon to learn that move.
                        //If the pokemon is from a player, prompt them. If they aren't, do not
                        if(playerPokemon) {
                            val bundle = Bundle()
                            bundle.putSerializable("newMove", newMovesList[0])
                            bundle.putSerializable("pokemon", pokemon)
                            val dialog = AddMoveDialogFragment()
                            dialog.arguments = bundle
                            newMovesList.clear()
                            dialog.isCancelable = false
                            dialog.show(fragmentManager!!, "AddMoveDialogFragment")
                        } else {
                            MoveLog.info("Trainer AI will always choose yes")

                            //Replace a move if pokemon already has 4 moves
                            if (pokemon.moves.count() == 4) {

                                //1 is always chosen for AI
                                val input = random.nextInt(5)
                                if(input in 1..4){
                                    val oldMove = pokemon.moves[input-1].name
                                    pokemon.moves[input-1] = newMovesList[0]
                                    MoveLog.info("$oldMove has been replaced by ${pokemon.moves[input-1].name}")
                                    newMovesList.removeFirst()
                                }

                            } else {
                                //Learn new move
                                pokemon.moves.add(newMovesList[0])
                                MoveLog.info(pokemon.battleStats.species + " has learned " + newMovesList[0].name)
                                newMovesList.removeFirst()
                            }
                        }
                    }
                }
                //If there are no new moves to be learned, print this message
                if(noNewMoves) {
                    MoveLog.info("$pokemonSpecies doesn't learn a new move at level $level")
                }
            }
        }
    }

    private suspend fun getNewMove(moveName: String, list: MutableList<Move>,
                                    gson: Gson, context: Context) = withContext(Dispatchers.IO) {
        var moveDataString: String = ""
        val job = launch {
            //Get the data
            val url: URL = URL("${PokeApiEndpoint.MOVE.url}/${moveName}")
            val data = Connector().connect(url) as String
            //Simplify it
            moveDataString = simplifyMove(data)
        }
        job.join()

        val move: Move = gson.fromJson(moveDataString, Move::class.java)
        move.pp = move.maxPP
        list.add(move)
    }

    fun addMoveOrSummonFragment(pokemon: Pokemon, newMove: Move){
        //Replace a move if pokemon already has 4 moves by summoning a DialogFragment
        if (pokemon.moves.count() == 4) {
            val bundle = Bundle()
            bundle.putSerializable("moves", pokemon.moves)
            bundle.putIntArray("movePosition", intArrayOf(0))
            val selectMoveFragment = SelectMovesFragment(false)
            selectMoveFragment.arguments = bundle
            selectMoveFragment.isCancelable = false
            selectMoveFragment.show(fragmentManager!!, "fragment")
        } else {
            //Learn new move
            pokemon.moves.add(newMove)
            MoveLog.info(pokemon.battleStats.species + " has learned " + newMove.name)
        }
    }

    fun replaceMove(pokemon: Pokemon, newMove: Move, position: Int){
        val oldMove = pokemon.moves[position].name
        pokemon.moves[position] = newMove
        MoveLog.info("$oldMove has been replaced by ${pokemon.moves[position].name}")
    }
}