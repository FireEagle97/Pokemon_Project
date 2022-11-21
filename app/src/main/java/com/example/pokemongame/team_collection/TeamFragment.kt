//package com.example.pokemongame.team_collection
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.pokemongame.R
//class TeamFragment : Fragment() {
//    private var pokemon: ArrayList<String>? = null
//    private var layoutManager: RecyclerView.LayoutManager? = null
//    private lateinit var recycler: RecyclerView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            pokemon = it.getSerializable("pokemon") as ArrayList<String>
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_team, container, false)
//    }
//
//    companion object {
//        fun newInstance(myList : ArrayList<String>): TeamFragment {
//            val args = Bundle()
//            args.putSerializable("pokemon",myList);
//            val fragment = TeamFragment()
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        recycler = view.findViewById(R.id.recycler_view)
//        recycler.layoutManager
//        if(pokemon != null) {
//            val adapter: PokemonTeamRecyclerAdapter = PokemonTeamRecyclerAdapter(
//                pokemon!!.toMutableList()
//            ){name: String ->
//                run {
////                    collectionAdapter.notifyItemInserted(collection.size - 1)
//                }}
//            recycler.adapter = adapter
//            layoutManager = LinearLayoutManager(context)
//            recycler.layoutManager = layoutManager
//        }
//        super.onViewCreated(view, savedInstanceState)
//    }
//
//}
//
