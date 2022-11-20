package com.example.pokemongame.team_collection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemongame.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "pokemon"

/**
 * A simple [Fragment] subclass.
 * Use the [TeamFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TeamFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var pokemon: ArrayList<String>? = null
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pokemon = it.getStringArrayList(ARG_PARAM1) as ArrayList<String>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TeamFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(pokemon: ArrayList<String>) =
            TeamFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_PARAM1, pokemon)
                }
            }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.findViewById(R.id.list)
        recycler.layoutManager
        if(pokemon != null) {
            val adapter: PokemonRecyclerAdapter = PokemonRecyclerAdapter(
                arguments?.getStringArrayList(
                    ARG_PARAM1)!!.toMutableList())
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()
        }


        super.onViewCreated(view, savedInstanceState)
    }

}

