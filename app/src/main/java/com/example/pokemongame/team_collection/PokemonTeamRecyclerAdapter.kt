package com.example.pokemongame.team_collection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemongame.databinding.PokemonItemTeamBinding
import com.example.pokemongame.pokemon.Pokemon

class PokemonTeamRecyclerAdapter(private val pokemonList: MutableList<Pokemon>, private val listener: (pokemon: Pokemon) -> Unit):
    RecyclerView.Adapter<PokemonTeamRecyclerAdapter.ViewHolder>(){

   inner class ViewHolder(val binding: PokemonItemTeamBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.moveOver.setOnClickListener(){
                if(pokemonList.size > 1) {
                    val temp = pokemonList[adapterPosition]
                    pokemonList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                    listener(temp)
                }
            }
            binding.moveDown.setOnClickListener(){
                if(adapterPosition < pokemonList.size -1){
                    val temp = pokemonList[adapterPosition + 1]
                    pokemonList[adapterPosition + 1] = pokemonList[adapterPosition]
                    pokemonList[adapterPosition] = temp
                    notifyItemRangeChanged(adapterPosition, 2)
            }

    }
            binding.moveUp.setOnClickListener(){
                if(adapterPosition > 0){
                    val temp = pokemonList[adapterPosition - 1]
                    pokemonList[adapterPosition - 1] = pokemonList[adapterPosition]
                    pokemonList[adapterPosition] = temp
                    notifyItemRangeChanged(adapterPosition -1, 2)
                }
        }
     }
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PokemonItemTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //this will need to be changed to use pokemon class
        val binding = holder.binding
        val pokemonInfo = pokemonList[position]
        binding.pokemonInfo.text = buildString {
        append(pokemonInfo.name)
        append(" ")
        append(pokemonInfo.hp)
    }


        }

    override fun getItemCount(): Int {
        return pokemonList.size
    }


}