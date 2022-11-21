package com.example.pokemongame.team_collection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemongame.R
import com.example.pokemongame.databinding.ActivityTeamBinding

class TeamActivity: AppCompatActivity() {
    private lateinit var binding: ActivityTeamBinding
//    private lateinit var adapter: PokemonRecyclerAdapter
    private lateinit var pokemon: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pokemon = mutableListOf<String>()
        pokemon.add("Obi-Wan")
        pokemon.add("Anakin")
        pokemon.add("Ahsoka")
        pokemon.add("Captain Rex")


        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = TeamFragment.newInstance(pokemon as ArrayList<String>)
        fragmentTransaction.add(R.id.team_fragment, fragment)
        fragmentTransaction.commit()

    }

//    override fun onStart() {
//        super.onStart()
//        pokemon = mutableListOf<String>()
//        pokemon.add("Obi-Wan")
//        pokemon.add("Anakin")
//        pokemon.add("Ahsoka")
//        pokemon.add("Captain Rex")
//        adapter = PokemonRecyclerAdapter(pokemon)
//        binding.pokemonItemList.adapter = adapter
//        binding.pokemonItemList.layoutManager = LinearLayoutManager(this)

//    }
}
