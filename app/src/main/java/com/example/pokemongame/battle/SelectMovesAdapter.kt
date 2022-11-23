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
import com.example.pokemongame.pokemon.Move


class SelectMovesAdapter(private val moves: ArrayList<Move>, var movePosition: IntArray, val dialogFragment: DialogFragment): RecyclerView.Adapter<SelectMovesAdapter.ViewHolder>(){
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val moveName : TextView = view.findViewById(R.id.moveName)
        var movePP : TextView = view.findViewById(R.id.movePP)
        val selectButton : Button = view.findViewById(R.id.select_button)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.select_move, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewholder: ViewHolder, position: Int){
        viewholder.moveName.text = moves[position].name
        viewholder.movePP.text = moves[position].pp.toString()
        //check if move can be selected
        viewholder.selectButton.setOnClickListener{
            if(moves[position].pp > 0){
                getMovePosition(position)
                dialogFragment.dismiss()
            }else {
                Toast.makeText(viewholder.itemView.context, "Your pokemon needs to rest to use this move", Toast.LENGTH_LONG).show()

            }
        }

    }

    override fun getItemCount(): Int {
        return moves.size
    }

    private fun getMovePosition(position: Int){
        movePosition[0] = position
    }
}