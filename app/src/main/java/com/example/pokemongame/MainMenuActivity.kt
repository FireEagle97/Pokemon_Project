package com.example.pokemongame

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemongame.databinding.ActivityMainMenuBinding
import com.example.pokemongame.pokemon.Pokemon
import com.example.pokemongame.team_collection.TeamActivity

private lateinit var binding: ActivityMainMenuBinding
private lateinit var team: ArrayList<Pokemon>
private lateinit var trainerName: String
private lateinit var collection: ArrayList<Pokemon>

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        team = intent.getSerializableExtra("team") as ArrayList<Pokemon> // I think we can assume team will never be empty.
        trainerName = intent.getStringExtra("trainerName").toString() // Similarly, user will have to have set a trainer name to get to this screen.
        collection = if(intent.hasExtra("collection")){
            intent.getSerializableExtra("collection") as ArrayList<Pokemon>
        } else{
            ArrayList<Pokemon>() // if not passed, create empty one
        }


        binding.pokeCenterBtn.setOnClickListener{
//            val intent = Intent(this, PokemonCenterActivity::class.java)
//            startActivity(intent)
            //call a method to reset pp to maxPP and hp to maxHp
            Toast.makeText(applicationContext, "Your pokemon team is ready to battle! ", Toast.LENGTH_SHORT).show()
        }
        binding.changeTeamBtn.setOnClickListener(){
            val intent = Intent(this, TeamActivity::class.java)
            startActivity(intent)
        }

    }
}