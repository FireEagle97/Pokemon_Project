package com.example.pokemongame.team_collection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemongame.BattlePhaseActivity
import com.example.pokemongame.databinding.PokemonItemCollectionBinding
import com.example.pokemongame.pokemon.Pokemon
import com.example.pokemongame.pokemon.getPokemonImageResourceId

class PokemonCollectionRecyclerAdapter(private val pokemonList: MutableList<Pokemon> ,  private val listener: (pokemon: Pokemon, position: Int) -> Unit):
    RecyclerView.Adapter<PokemonCollectionRecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: PokemonItemCollectionBinding) : RecyclerView.ViewHolder(binding.root){
        init {

            binding.move.setOnClickListener(){
                    val pokemonToMove = pokemonList[adapterPosition]
                    listener(pokemonToMove, adapterPosition) // what to do defined in TeamActivity
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
        // WILL NEED TO DECIDE HOW TO DISPLAY POKEMON (WHAT INFO)
        binding.pokemonName.text = pokemonInfo.name
        binding.hp.text = buildString {
            append(pokemonInfo.hp.toString())
            append(" / ")
            append(pokemonInfo.maxHp.toString())
            append("HP")
        }
        binding.lvl.text = buildString {
            append("lvl ")
            append(pokemonInfo.level.toString())
        }
        binding.species.text =  buildString {
            append("(")
            append(pokemonInfo.battleStats.species.toString())
            append(")")
        }
        binding.sprite.setImageBitmap(BattlePhaseActivity().getPokemonBitMap(pokemonInfo.frontSprite))
    }


}