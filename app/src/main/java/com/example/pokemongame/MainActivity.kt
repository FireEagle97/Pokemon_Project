package com.example.pokemongame


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.pokemongame.battle.DamageCalculations
import com.example.pokemongame.databinding.ActivityMainBinding
import com.example.pokemongame.pokemon.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.logging.Logger



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase

    companion object {
        val mainLog: Logger = Logger.getLogger(MainActivity::class.java.name)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "name"
        ).build()
    }

    //Intro screen
    override fun onStart() {
        super.onStart()
        binding.start.setOnClickListener() {
            var teamAndCollection = ArrayList<List<Pokemon>>()
            runBlocking {
                if (checkDBNotEmpty()) {
                    teamAndCollection = getTeamAndCol()
//                    intent = Intent(this, MainMenuActivity::class.java)

                }
            }
            if(teamAndCollection.size > 0){
                intent = Intent(this, MainMenuActivity::class.java)
                intent.putExtra("team", teamAndCollection[0] as ArrayList<Pokemon>)
                intent.putExtra("collection", teamAndCollection[1] as ArrayList<Pokemon>)
                intent.putExtra("trainerName", "TO REPLACE")
            }
            else{
                intent = Intent(this, RegisterActivity::class.java)
            }
            startActivity(intent)
        }
    }

    private suspend fun checkDBNotEmpty(): Boolean = withContext(Dispatchers.IO) {
        return@withContext (db.PokemonDao().getTeam().isNotEmpty())
    }

    private suspend fun getTeamAndCol(): ArrayList<List<Pokemon>> = withContext(Dispatchers.IO) {
        val teamAndCollection = ArrayList<List<Pokemon>>()
        teamAndCollection.add(db.PokemonDao().getTeam())
        teamAndCollection.add(db.PokemonDao().getCollection())
        for(i in 0 until teamAndCollection.size){
            for(k in 0 until teamAndCollection[i].size){
                teamAndCollection[i][k].battleStats = db.BattleStatsDao().getBattleStats(teamAndCollection[i][k].species)

            }
        }
        return@withContext teamAndCollection
    }
}