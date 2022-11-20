package com.example.pokemongame.team_collection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemongame.databinding.ActivityTeamBinding

class TeamActivity: AppCompatActivity() {
    private lateinit var binding: ActivityTeamBinding
    private lateinit var adapter: PokemonRecyclerAdapter
    private lateinit var pokemon: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()
        pokemon = mutableListOf<String>()
        pokemon.add("Obi-Wan")
        pokemon.add("Anakin")
        pokemon.add("Ahsoka")
        pokemon.add("Captain Rex")
        adapter = PokemonRecyclerAdapter(pokemon)
        binding.pokemonItemList.adapter = adapter
        binding.pokemonItemList.layoutManager = LinearLayoutManager(this)

    }
}
