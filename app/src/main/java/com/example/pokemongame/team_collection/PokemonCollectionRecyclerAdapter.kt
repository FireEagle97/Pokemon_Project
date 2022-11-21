package com.example.pokemongame.team_collection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemongame.databinding.PokemonItemCollectionBinding

class PokemonCollectionRecyclerAdapter(private val pokemonList: MutableList<String>):
    RecyclerView.Adapter<PokemonCollectionRecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: PokemonItemCollectionBinding) : RecyclerView.ViewHolder(binding.root){
        init {

            binding.moveDown.setOnClickListener(){
                if(adapterPosition < pokemonList.size -1){
                    val temp:String = pokemonList[adapterPosition + 1]
                    pokemonList[adapterPosition + 1] = pokemonList[adapterPosition]
                    pokemonList[adapterPosition] = temp
                    notifyItemRangeChanged(adapterPosition, 2)
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