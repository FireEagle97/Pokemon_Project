package com.example.pokemongame.team_collection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemongame.databinding.PokemonItemCollectionBinding
import com.example.pokemongame.pokemon.Pokemon
import com.example.pokemongame.pokemon.getPokemonImageResourceId

class PokemonCollectionRecyclerAdapter(private val pokemonList: MutableList<Pokemon> ,  private val listener: (pokemon: Pokemon, position: Int) -> Unit):
    RecyclerView.Adapter<PokemonCollectionRecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: PokemonItemCollectionBinding) : RecyclerView.ViewHolder(binding.root){
        init {

            binding.move.setOnClickListener(){
                    val pokemonToMove = pokemonList[adapterPosition]
                    listener(pokemonToMove, adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PokemonItemCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val pokemonInfo = pokemonList[position]
        binding.pokemonInfo.text = buildString {
        append(pokemonInfo.name)
        append(" ")
        append(pokemonInfo.hp)
    }
        binding.pokemonInfo.setCompoundDrawablesWithIntrinsicBounds(0,0,getPokemonImageResourceId(pokemonInfo.species),0,)
    }


}