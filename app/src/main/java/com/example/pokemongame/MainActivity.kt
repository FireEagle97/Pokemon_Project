package com.example.pokemongame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pokemongame.pokemon.MoveAssigner
import com.example.pokemongame.pokemon.Pokemon

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dude = Pokemon("charmander", null);
        val moveAssigner = MoveAssigner().assignNewMoves(dude, 1, applicationContext)
    }

}