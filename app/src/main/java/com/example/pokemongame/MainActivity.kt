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

        val charmander = PokemonCreator().createPokemon(15,"charmander",applicationContext)
        val squirtle = PokemonCreator().createPokemon(15, "squirtle", applicationContext)
        mainLog.info(charmander.toString())

        //Test levels (a pokemon needs to be created first)
        //Level().initializeLevels(charmander, charmander.level, applicationContext)
        //Level().initializeLevels(squirtle, squirtle.level, applicationContext)
        //MoveAssigner.MoveLog.info(charmander.moves.toString())

        //Test damage calculation (with a charmander level 15 vs a squirtle level 15. Charmander's ember damage should be 6)
        //val damage = DamageCalculations().calculateDamage(charmander, charmander.moves[2], squirtle, applicationContext)
        //mainLog.info("damage: ${damage.toString()}")

        //test/assign moves. Can be moved elsewhere
        //MoveAssigner().assignNewMoves(charmander, 15, applicationContext)
        //MoveAssigner().assignNewMoves(charmander, 15, applicationContext)

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