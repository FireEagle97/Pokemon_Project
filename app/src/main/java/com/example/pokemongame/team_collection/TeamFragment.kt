package com.example.pokemongame.team_collection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
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
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pokemon = it.getSerializable("pokemon") as ArrayList<String>
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
        fun newInstance(myList : ArrayList<String>): TeamFragment {
            val args = Bundle()
            args.putSerializable("pokemon",myList);
            val fragment = TeamFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.findViewById(R.id.recycler_view)
        recycler.layoutManager
        if(pokemon != null) {
            val adapter: PokemonRecyclerAdapter = PokemonRecyclerAdapter(
                pokemon!!.toMutableList()
            )
            recycler.adapter = adapter
            layoutManager = LinearLayoutManager(context)
            recycler.layoutManager = layoutManager
            adapter.notifyDataSetChanged()
        }
        Toast.makeText(context, pokemon?.get(0) ?: "Can't find", Toast.LENGTH_LONG).show()

//        adapter = PokemonRecyclerAdapter(pokemon)
//        binding.pokemonItemList.adapter = adapter
//        binding.pokemonItemList.layoutManager = LinearLayoutManager(this)

        super.onViewCreated(view, savedInstanceState)
    }

}

