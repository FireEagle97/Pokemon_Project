package com.example.pokemongame.battle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemongame.R
import com.example.pokemongame.pokemon.Pokemon
import kotlin.coroutines.coroutineContext

class SwitchAdapter(private val team: ArrayList<Pokemon>, val dialogFragment: DialogFragment): RecyclerView.Adapter<SwitchAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val pokemonName: TextView = view.findViewById(R.id.pokemonName)
        val pokemonHP: TextView = view.findViewById(R.id.pokemonHP)
        val switchButton: Button = view.findViewById(R.id.switch_button)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.switch_pokemon, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int){
        viewHolder.pokemonName.text = team[position].name
        viewHolder.pokemonHP.text = team[position].hp.toString()
        viewHolder.switchButton.setOnClickListener{
            if(team[position].hp > 0){
                flipWillSwitchPokemon(position)
                dialogFragment.dismiss()
            } else {
                Toast.makeText(viewHolder.itemView.context, "You cannot switch-in a pokemon with no HP remaining", Toast.LENGTH_SHORT)
            }
        }
    }

    override fun getItemCount(): Int {
        return team.size
    }

    fun flipWillSwitchPokemon(position: Int) {
        team[position].willBeSwitched = true
    }
}

