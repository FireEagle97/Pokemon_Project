package com.example.pokemongame.team_collection

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemongame.R
import com.example.pokemongame.databinding.ActivityTeamCollectionBinding

class TeamActivity: AppCompatActivity() {
    private lateinit var binding: ActivityTeamCollectionBinding

    private lateinit var pokemon: MutableList<String>
    private lateinit var collection: MutableList<String>
    private lateinit var collectionAdapter: PokemonCollectionRecyclerAdapter
    private lateinit var teamAdapter: PokemonTeamRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeamCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState == null) {

            pokemon = mutableListOf<String>()
            pokemon.add("Obi-Wan")
            pokemon.add("Anakin")
            pokemon.add("Ahsoka")
            pokemon.add("Captain Rex")
            collection = mutableListOf<String>("Arya Stark", "Tyrion Lannister", "Euron Greyjoy", "Daenerys")

            teamAdapter = PokemonTeamRecyclerAdapter(pokemon) { name: String ->
                run {
                        collection.add(name)
                        collectionAdapter.notifyItemInserted(collection.size - 1)
                }
            }

            binding.recyclerView.adapter = teamAdapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            collectionAdapter = PokemonCollectionRecyclerAdapter(collection) { name: String, pos: Int ->
                run {
                    if(pokemon.size < 6){
                    collection.removeAt(pos)
                    collectionAdapter.notifyItemRemoved(pos)
                    pokemon.add(name)
                    teamAdapter.notifyItemInserted(pokemon.size - 1)
                }
                }
            }
            binding.collectionRecyclerView.adapter = collectionAdapter
            binding.collectionRecyclerView.layoutManager = LinearLayoutManager(this)






        }


    }


}
