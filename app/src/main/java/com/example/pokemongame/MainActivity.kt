package com.example.pokemongame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pokemongame.pokemon.Pokemon
import com.example.pokemongame.pokemon.PokemonAssigner
import com.example.pokemongame.databinding.ActivityMainBinding;
private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val test = Pokemon(4,"charmander")

        val pokemon = PokemonAssigner().createPokemon(test, applicationContext)
        Toast.makeText(applicationContext,pokemon.types[0], Toast.LENGTH_SHORT).show()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    override fun onStart() {
        super.onStart()
        binding.start.setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}