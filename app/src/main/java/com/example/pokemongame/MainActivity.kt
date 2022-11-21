package com.example.pokemongame


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemongame.battle.DamageCalculations
import com.example.pokemongame.databinding.ActivityMainBinding
import com.example.pokemongame.pokemon.Level
import com.example.pokemongame.pokemon.MoveAssigner
import com.example.pokemongame.pokemon.PokemonCreator
import java.util.logging.Logger


private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object{
        val mainLog : Logger = Logger.getLogger(MainActivity::class.java.name)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    //Intro screen
    override fun onStart() {
        super.onStart()
        binding.start.setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

}