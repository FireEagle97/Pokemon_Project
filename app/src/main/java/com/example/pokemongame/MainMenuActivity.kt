package com.example.pokemongame

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.pokemongame.databinding.ActivityMainMenuBinding
import com.example.pokemongame.pokemon.*
import com.example.pokemongame.team_collection.TeamActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private lateinit var db: AppDatabase



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getDatabase(applicationContext)

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
        binding.wildBattleBtn.setOnClickListener {
            val intent = Intent(this, BattlePhaseActivity::class.java)
            intent.putExtra("team", team)
            intent.putExtra("collection", collection)
            intent.putExtra("trainerName", trainerName)
            intent.putExtra("inTrainerBattle", false)
            startActivityForResult(intent, REQ_CODE)
        }

        binding.trainerBattleBtn.setOnClickListener {
            val intent = Intent(this, BattlePhaseActivity::class.java)
            intent.putExtra("team", team)
            intent.putExtra("collection", collection)
            intent.putExtra("trainerName", trainerName)
            intent.putExtra("inTrainerBattle", true)
            startActivityForResult(intent, REQ_CODE)
        }

        binding.saveBtn!!.setOnClickListener {
            lifecycleScope.launch {
                save()
                Toast.makeText(applicationContext, "Saved game state to the database", Toast.LENGTH_SHORT).show()

            }

        }
    }

    private suspend fun save() = withContext(Dispatchers.IO) {

        saveToDB(team, collection, db)
        db.TrainerNameDao().insert(TrainerName(trainerName))
        val teamAndCollection = getTeamAndCol(db)
        //resetting to database values to prevent from repeatedly saving same value.
        team = teamAndCollection[0] as ArrayList<Pokemon>
        collection = teamAndCollection[1] as ArrayList<Pokemon>

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
