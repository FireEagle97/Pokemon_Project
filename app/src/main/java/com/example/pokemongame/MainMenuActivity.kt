package com.example.pokemongame

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemongame.databinding.ActivityMainMenuBinding
import com.example.pokemongame.pokemon.Level
import com.example.pokemongame.pokemon.Pokemon
import com.example.pokemongame.pokemon.PokemonCreator
import com.example.pokemongame.team_collection.TeamActivity
import java.util.logging.Logger


class MainMenuActivity : AppCompatActivity() {
    companion object {
        private const val LOG_TAG = "M_M_ACTIVITY_DEV_LOG"
        val mainMenuLog : Logger = Logger.getLogger(MainMenuActivity::class.java.name)
        private const val REQ_CODE = 1234
    }
    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var team: ArrayList<Pokemon>
    private lateinit var trainerName: String
    private lateinit var collection: ArrayList<Pokemon>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        team =
            intent.getSerializableExtra("team") as ArrayList<Pokemon> // I think we can assume team will never be empty.
        trainerName = intent.getStringExtra("trainerName")
            .toString() // Similarly, user will have to have set a trainer name to get to this screen.
        collection = if (intent.hasExtra("collection")) {
            intent.getSerializableExtra("collection") as ArrayList<Pokemon>
        } else {
            ArrayList<Pokemon>()
           // if not passed, create empty one
        }
        //in here for testing purposes, just to see that colelction also works
//        val pokemon = PokemonCreator().createPokemon(3, "squirtle",applicationContext)
//        Level().initializeLevels(pokemon,pokemon.level,applicationContext)
//        pokemon.hp = pokemon.maxHp
//        collection.add(pokemon)
        val pokemon1 = PokemonCreator().createPokemon(3, "squirtle",applicationContext)
        Level().initializeLevels(pokemon1,pokemon1.level,applicationContext)
        pokemon1.hp = pokemon1.maxHp
        val pokemon2 = PokemonCreator().createPokemon(4, "bulbasaur",applicationContext)
        Level().initializeLevels(pokemon2,pokemon2.level,applicationContext)
        pokemon2.hp = pokemon2.maxHp
        val pokemon3 = PokemonCreator().createPokemon(3, "squirtle",applicationContext)
        Level().initializeLevels(pokemon3,pokemon3.level,applicationContext)
        pokemon2.hp = pokemon2.maxHp
        val pokemon4 = PokemonCreator().createPokemon(3, "charmander",applicationContext)
        Level().initializeLevels(pokemon4,pokemon4.level,applicationContext)
        pokemon4.hp = pokemon4.maxHp
        val pokemon5 = PokemonCreator().createPokemon(3, "squirtle",applicationContext)
        Level().initializeLevels(pokemon5,pokemon5.level,applicationContext)
        pokemon5.hp = pokemon5.maxHp
        team.add(pokemon5)
        team.add(pokemon4)
        team.add(pokemon3)
        team.add(pokemon2)
        team.add(pokemon1)


        binding.pokeCenterBtn.setOnClickListener {
            for (pokemon in team) {
                pokemon.hp = pokemon.maxHp
                pokemon.moves.forEachIndexed { index, move ->
                    move.pp = move.maxPP
                }
            }
            Toast.makeText(
                applicationContext,
                "Your pokemon team has been restored to full health! ",
                Toast.LENGTH_SHORT
            ).show()
        }
        //pass this data to activity where user swaps between team and collection
        binding.changeTeamBtn.setOnClickListener() {
            val intent = Intent(this, TeamActivity::class.java)
            intent.putExtra("team", team)
            intent.putExtra("collection", collection)
            intent.putExtra("trainerName", trainerName)
            startActivityForResult(intent, REQ_CODE)
        }
//        binding.trainerBattleBtn.setOnClickListener{
//            val intent = Intent(this, BattlePhaseActivity::class.java)
//            startActivity(intent)
//        }

        //Button for wild battles
        binding.wildBattleBtn.setOnClickListener{
            val intent = Intent(this, TrainerBattleActivity::class.java)
            intent.putExtra("team", team)
            intent.putExtra("collection", collection)
            intent.putExtra("trainerName", trainerName)
            intent.putExtra("inTrainerBattle", false)
            startActivityForResult(intent, REQ_CODE)
        }

        binding.trainerBattleBtn.setOnClickListener{
            val intent = Intent(this, TrainerBattleActivity::class.java)
            intent.putExtra("team", team)
            intent.putExtra("collection", collection)
            intent.putExtra("trainerName", trainerName)
            intent.putExtra("inTrainerBattle", true)
            startActivityForResult(intent, REQ_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE) {
                team = data!!.getSerializableExtra("team") as ArrayList<Pokemon>
                collection = data.getSerializableExtra("collection") as ArrayList<Pokemon>
                trainerName = data.getStringExtra("trainerName").toString()
            }
            else {
                Log.w(LOG_TAG, "Returning from an unknown activity")
            }
        }
     else {
        Log.w(LOG_TAG, "Activity result was not 'Activity.RESULT_OK' (-1), was '$resultCode'")
    }

    super.onActivityResult(requestCode, resultCode, data)
        }
    }
