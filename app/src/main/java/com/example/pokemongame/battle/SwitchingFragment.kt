package com.example.pokemongame.battle

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemongame.R
import com.example.pokemongame.pokemon.Pokemon

class SwitchingFragment(): DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.switch_fragment, container, false)
        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        val team: ArrayList<Pokemon> = requireArguments().getSerializable("team") as ArrayList<Pokemon>
        recyclerView.adapter = SwitchAdapter(team)
        return view
    }
    companion object {
        const val TAG = "SwitchDialog"
    }
}