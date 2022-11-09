package com.example.pokemongame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pokemongame.pokemon.Pokemon
import com.example.pokemongame.pokemon.PokemonAssigner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val test = Pokemon(4,"charmander")

        val pokemonAssigner = PokemonAssigner().setAttributes(test.species, applicationContext)
        Toast.makeText(applicationContext,pokemonAssigner, Toast.LENGTH_SHORT).show()
    }
}