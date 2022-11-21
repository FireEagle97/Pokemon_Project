package com.example.pokemongame.team_collection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemongame.databinding.ActivityTeamCollectionBinding
import com.example.pokemongame.pokemon.Pokemon
import com.example.pokemongame.pokemon.PokemonCreator

class TeamActivity: AppCompatActivity() {
    private lateinit var binding: ActivityTeamCollectionBinding

//    private lateinit var pokemon: MutableList<String>
//    private lateinit var collection: MutableList<String>
    private lateinit var collection: MutableList<Pokemon>
    private lateinit var team: MutableList<Pokemon>
    private lateinit var collectionAdapter: PokemonCollectionRecyclerAdapter
    private lateinit var teamAdapter: PokemonTeamRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeamCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState == null) {
            team = mutableListOf<Pokemon>()
//            val charmander = PokemonCreator().createPokemon(15,"charmander",applicationContext)

            team.add(PokemonCreator().createPokemon(4, "charmander", applicationContext))
            team.add(PokemonCreator().createPokemon(4, "bulbasaur", applicationContext))
            team.add(PokemonCreator().createPokemon(4, "charmander", applicationContext))
            team.add(PokemonCreator().createPokemon(4, "squirtle", applicationContext))

            collection = mutableListOf<Pokemon>()
            collection.add(PokemonCreator().createPokemon(4, "squirtle", applicationContext))
            collection.add(PokemonCreator().createPokemon(3, "squirtle", applicationContext))
            collection.add(PokemonCreator().createPokemon(3, "squirtle", applicationContext))
            collection.add(PokemonCreator().createPokemon(3, "squirtle", applicationContext))

            collectionAdapter = PokemonCollectionRecyclerAdapter(collection) { pokemon: Pokemon, pos: Int ->
                run {
                    if(team.size < 6){
                    collection.removeAt(pos)
                    collectionAdapter.notifyItemRemoved(pos)
                    team.add(pokemon)
                    teamAdapter.notifyItemInserted(team.size - 1)
                }
                }
            }
            teamAdapter = PokemonTeamRecyclerAdapter(team) { pokemon: Pokemon ->
                run {
                        collection.add(pokemon)
                        collectionAdapter.notifyItemInserted(collection.size - 1)
                }
            }
            binding.recyclerView.adapter = teamAdapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.collectionRecyclerView.adapter = collectionAdapter
            binding.collectionRecyclerView.layoutManager = LinearLayoutManager(this)
//            binding.collectionRecyclerView.adapter = collectionAdapter
//            binding.collectionRecyclerView.layoutManager = LinearLayoutManager(this)



//            pokemon = mutableListOf<String>()
//            pokemon.add("Obi-Wan")
//            pokemon.add("Anakin")
//            pokemon.add("Ahsoka")
//            pokemon.add("Captain Rex")
//            collection = mutableListOf<String>("Arya Stark", "Tyrion Lannister", "Euron Greyjoy", "Daenerys")
//
//            teamAdapter = PokemonTeamRecyclerAdapter(pokemon) { name: String ->
//                run {
//                        collection.add(name)
//                        collectionAdapter.notifyItemInserted(collection.size - 1)
//                }
//            }
//
//            binding.recyclerView.adapter = teamAdapter
//            binding.recyclerView.layoutManager = LinearLayoutManager(this)
//            collectionAdapter = PokemonCollectionRecyclerAdapter(collection) { name: String, pos: Int ->
//                run {
//                    if(pokemon.size < 6){
//                    collection.removeAt(pos)
//                    collectionAdapter.notifyItemRemoved(pos)
//                    pokemon.add(name)
//                    teamAdapter.notifyItemInserted(pokemon.size - 1)
//                }
//                }
//            }
//            binding.collectionRecyclerView.adapter = collectionAdapter
//            binding.collectionRecyclerView.layoutManager = LinearLayoutManager(this)






        }


    }


}
