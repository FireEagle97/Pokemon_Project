package com.example.pokemongame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pokemongame.pokemon.BattleStatsGetter
import com.example.pokemongame.pokemon.BattleStats
import com.example.pokemongame.databinding.ActivityMainBinding;

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val test = BattleStats(4,"charmander")

        val pokemon = BattleStatsGetter().getPokemonBattleStats("charmander", applicationContext)
//        Toast.makeText(applicationContext,pokemon, Toast.LENGTH_SHORT).show()
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