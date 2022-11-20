package com.example.pokemongame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import  com.example.pokemongame.databinding.ActivityPokemonCenterBinding

private lateinit var binding: ActivityPokemonCenterBinding
class PokemonCenterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}