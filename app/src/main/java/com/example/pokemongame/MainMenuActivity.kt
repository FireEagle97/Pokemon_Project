package com.example.pokemongame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import  com.example.pokemongame.databinding.ActivityMainMenuBinding
import com.example.pokemongame.team_collection.TeamActivity

private lateinit var binding: ActivityMainMenuBinding

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pokeCenterBtn.setOnClickListener{
            val intent = Intent(this, PokemonCenterActivity::class.java)
            startActivity(intent)
        }
        binding.changeTeamBtn.setOnClickListener(){
            val intent = Intent(this, TeamActivity::class.java)
            startActivity(intent)
        }

    }
}