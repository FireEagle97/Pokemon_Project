package com.example.pokemongame.battle

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemongame.R
import com.example.pokemongame.pokemon.Move

class SelectMovesFragment(val battle: Boolean) : DialogFragment(){
    lateinit var movesList: ArrayList<Move>
    lateinit var movePosition : IntArray


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        super.onCreate(savedInstanceState)
        val view : View = inflater.inflate(R.layout.select_move_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.selectRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        movesList = requireArguments().getSerializable("moves") as ArrayList<Move>
        movePosition = requireArguments().getIntArray("movePosition") as IntArray

        //Assign the adapter with the returned objects
        recyclerView.adapter = SelectMovesAdapter(movesList,movePosition,this, battle)
        return view
    }
    override fun onDismiss(dialog: DialogInterface){
        super.onDismiss(dialog)
        if(battle) {
            setFragmentResult("movePosition", bundleOf("movePosition" to movePosition[0]))
        } else {
            setFragmentResult("newMovePosition", bundleOf("movePosition" to movePosition[0]))
        }
    }
}