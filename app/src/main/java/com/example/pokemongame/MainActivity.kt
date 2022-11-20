package com.example.pokemongame


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemongame.databinding.ActivityMainBinding
import com.example.pokemongame.pokemon.BattleStatsGetter


private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val test = BattleStats(4,"charmander")
        val battleStats = BattleStatsGetter().getPokemonBattleStats("charmander", applicationContext)

        Toast.makeText(applicationContext,battleStats.baseStatDefense.toString(), Toast.LENGTH_SHORT).show()

        //Code to test/assign moves. Can be moved elsewhere
//        val dude = Pokemon("charmander", mutableListOf());
//        MoveAssigner().assignNewMoves(dude, 15, applicationContext)
//        MoveAssigner().assignNewMoves(dude, 15, applicationContext)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    //Into screen
    override fun onStart() {
        super.onStart()
        binding.start.setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

}