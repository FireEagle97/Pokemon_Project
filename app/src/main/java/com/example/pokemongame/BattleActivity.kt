package com.example.pokemongame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemongame.battle.Battle_Phase
import com.example.pokemongame.battle.SwitchingFragment
import com.example.pokemongame.databinding.BattleActivityBinding
import com.example.pokemongame.pokemon.Level
import com.example.pokemongame.pokemon.Pokemon
import com.example.pokemongame.pokemon.PokemonCreator

private lateinit var binding: BattleActivityBinding

class BattleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = BattleActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        val charmander = PokemonCreator().createPokemon(15,"charmander",applicationContext)
        val squirtle = PokemonCreator().createPokemon(15, "squirtle", applicationContext)
        Level().initializeLevels(charmander, charmander.level, applicationContext)
        Level().initializeLevels(squirtle, squirtle.level, applicationContext)
        var team: ArrayList<Pokemon> =  arrayListOf(squirtle, charmander)
        Battle_Phase.BattleLog.info(team[0].hp.toString()+" "+ team[1].hp.toString())

        val bundle = Bundle()
        bundle.putSerializable("team", team)
        val fragmentManager = supportFragmentManager
        val switchFragment = SwitchingFragment()
        switchFragment.arguments = bundle


        binding.button.setOnClickListener {
            switchFragment.show(fragmentManager, SwitchingFragment.TAG)
        }
    }
}