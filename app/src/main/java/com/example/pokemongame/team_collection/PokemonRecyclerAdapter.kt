package com.example.pokemongame.team_collection

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemongame.databinding.PokemonItemBinding

class PokemonRecyclerAdapter(private val resources: Resources,
                             private val pokemonList: MutableList<String>
):
        RecyclerView.Adapter<PokemonRecyclerAdapter.ViewHolder>(){
    class ViewHolder(val binding: PokemonItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Here we create the binding from scratch, for a new ViewHolder
        val binding =
            PokemonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //this will need to be changed to use pokemon class
        val binding = holder.binding
        val pokemonInfo = pokemonList[position]
        binding.pokemonInfo.text = pokemonInfo
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }


}