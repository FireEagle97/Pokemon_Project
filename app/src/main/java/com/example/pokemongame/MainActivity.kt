package com.example.pokemongame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pokemongame.pokemon.MoveAssigner
import com.example.pokemongame.pokemon.Pokemon
import com.example.pokemongame.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Code to test/assign moves. Can be moved elsewhere
//        val dude = Pokemon("bulbasaur", mutableListOf());
//        MoveAssigner().assignNewMoves(dude, 15, applicationContext)
//        MoveAssigner().assignNewMoves(dude, 15, applicationContext)
//        MoveAssigner.MoveLog.info(dude.moves.toString())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    //Into screen
//    override fun onStart() {
//        super.onStart()
//        binding.start.setOnClickListener(){
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
//        }
//    }

}