package com.example.pokemongame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemongame.databinding.ActivityMainMenuBinding
import com.example.pokemongame.pokemon.Level
import com.example.pokemongame.pokemon.Pokemon
import com.example.pokemongame.pokemon.PokemonCreator
import com.example.pokemongame.team_collection.TeamActivity

class MainMenuActivity : AppCompatActivity() {
    companion object {
        private const val LOG_TAG = "M_M_ACTIVITY_DEV_LOG"
        private const val REQ_CODE = 1234
    }
    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var team: ArrayList<Pokemon>
    private lateinit var trainerName: String
    private lateinit var collection: ArrayList<Pokemon>

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






        binding.pokeCenterBtn.setOnClickListener {
//            val intent = Intent(this, PokemonCenterActivity::class.java)
//            startActivity(intent)
            //call a method to reset pp to maxPP and hp to maxHp
            Toast.makeText(
                applicationContext,
                "Your pokemon team is ready to battle! ",
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

        //Button for wild battles
        binding.wildBattleBtn.setOnClickListener{
            val intent = Intent(this, BattleActivity::class.java)
            intent.putExtra("team", team)
            intent.putExtra("collection", collection)
            intent.putExtra("trainerName", trainerName)
            intent.putExtra("inTrainerBattle", false)
            startActivityForResult(intent, REQ_CODE)
        }

        binding.trainerBattleBtn.setOnClickListener{
            val intent = Intent(this, BattleActivity::class.java)
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
