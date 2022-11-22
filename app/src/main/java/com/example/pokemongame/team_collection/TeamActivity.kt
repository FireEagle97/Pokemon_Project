package com.example.pokemongame.team_collection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemongame.databinding.ActivityTeamCollectionBinding
import com.example.pokemongame.pokemon.Pokemon
import com.example.pokemongame.pokemon.PokemonCreator

class TeamActivity: AppCompatActivity() {
    private lateinit var binding: ActivityTeamCollectionBinding
    private lateinit var collection: MutableList<Pokemon>
    private lateinit var team: MutableList<Pokemon>
    private lateinit var trainerName: String
    private lateinit var collectionAdapter: PokemonCollectionRecyclerAdapter
    private lateinit var teamAdapter: PokemonTeamRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeamCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
            team = intent.getSerializableExtra("team") as ArrayList<Pokemon>
            collection = intent.getSerializableExtra("collection") as ArrayList<Pokemon>
            trainerName = intent.getStringExtra("trainerName").toString()


        //create the adapters for team and collection
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
                        collectionAdapter.notifyItemInserted(collection.size - 1) //callback functions used to modify recycler views from one another
                }
            }

            binding.recyclerView.adapter = teamAdapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.collectionRecyclerView.adapter = collectionAdapter
            binding.collectionRecyclerView.layoutManager = LinearLayoutManager(this)
            //pass updated lists back
            binding.save.setOnClickListener(){
                intent.putExtra("collection", collection as ArrayList<Pokemon>)
                intent.putExtra("team", team as ArrayList<Pokemon>)
                intent.putExtra("trainerName", "NEW NAME")
                setResult(RESULT_OK, intent)
                finish()
            }
    }


}
