package com.example.pokemongame.battle

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemongame.R
import com.example.pokemongame.pokemon.Pokemon

class SwitchingFragment(): DialogFragment() {
    lateinit var pokemonTeam: ArrayList<Pokemon>
    lateinit var teamPosition: Array<Int>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.switch_fragment, container, false)
        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        pokemonTeam = requireArguments().getSerializable("team") as ArrayList<Pokemon>
        teamPosition = requireArguments().getSerializable("teamPosition") as Array<Int>
        recyclerView.adapter = SwitchAdapter(pokemonTeam, teamPosition,this)
        return view
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        setFragmentResult("teamPosition", bundleOf("teamPosition" to teamPosition))
    }
}