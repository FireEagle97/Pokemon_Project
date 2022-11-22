package com.example.pokemongame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pokemongame.databinding.ActivityRegisterBinding
import com.example.pokemongame.pokemon.Pokemon
import com.example.pokemongame.pokemon.PokemonCreator

private lateinit var binding: ActivityRegisterBinding
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Three default pokemon for the user to choose from
        val charmander = PokemonCreator().createPokemon(5,"charmander",applicationContext)
        val squirtle = PokemonCreator().createPokemon(5,"squirtle",applicationContext)
        val bulbasaur =PokemonCreator().createPokemon(5,"bulbasaur",applicationContext)
        val team = ArrayList<Pokemon>()
        binding.menuBtn?.setOnClickListener{
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }
    }

}
