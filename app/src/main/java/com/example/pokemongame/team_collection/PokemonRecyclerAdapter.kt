package com.example.pokemongame.team_collection

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemongame.databinding.PokemonItemBinding

class PokemonRecyclerAdapter(private val pokemonList: MutableList<String>):
    RecyclerView.Adapter<PokemonRecyclerAdapter.ViewHolder>(){

   inner class ViewHolder(val binding: PokemonItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.moveOver.setOnClickListener(){
                if(pokemonList.size > 1) {
                    pokemonList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                }
            }
            binding.moveDown.setOnClickListener(){
                if(adapterPosition < pokemonList.size -1){
                    val temp:String = pokemonList[adapterPosition + 1]
                    pokemonList[adapterPosition + 1] = pokemonList[adapterPosition]
                    pokemonList[adapterPosition] = temp
                    notifyItemRangeChanged(adapterPosition, 2)
            }

    }
            binding.moveUp.setOnClickListener(){
                if(adapterPosition > 0){
                    val temp:String = pokemonList[adapterPosition - 1]
                    pokemonList[adapterPosition - 1] = pokemonList[adapterPosition]
                    pokemonList[adapterPosition] = temp
                    notifyItemRangeChanged(adapterPosition -1, 2)
                }
        }
   }
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PokemonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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