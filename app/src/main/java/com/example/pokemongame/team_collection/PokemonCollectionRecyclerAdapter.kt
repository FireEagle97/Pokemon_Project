package com.example.pokemongame.team_collection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemongame.databinding.PokemonItemCollectionBinding

class PokemonCollectionRecyclerAdapter(private val pokemonList: MutableList<String> , private val teamList: MutableList<String>, private val listener: (position: Int) -> Unit):
    RecyclerView.Adapter<PokemonCollectionRecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: PokemonItemCollectionBinding) : RecyclerView.ViewHolder(binding.root){
        init {

            binding.moveDown.setOnClickListener(){
                if(pokemonList.size > 1 && teamList.size < 6) {
                    var temp = pokemonList[adapterPosition]
                    pokemonList.removeAt(adapterPosition)
                    teamList.add(temp)
                    notifyItemRemoved(adapterPosition)
                    listener(teamList.size-1)
                }

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
        //this will need to be changed to use pokemon class
        val binding = holder.binding
        val pokemonInfo = pokemonList[position]
        binding.pokemonInfo.text = pokemonInfo
    }


}