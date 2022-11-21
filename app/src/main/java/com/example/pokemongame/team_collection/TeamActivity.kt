package com.example.pokemongame.team_collection

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemongame.R
import com.example.pokemongame.databinding.ActivityTeamCollectionBinding

class TeamActivity: AppCompatActivity() {
    private lateinit var binding: ActivityTeamCollectionBinding

    private lateinit var pokemon: MutableList<String>
    private lateinit var collection: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeamCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState == null) {

            pokemon = mutableListOf<String>()
            pokemon.add("Obi-Wan")
            pokemon.add("Anakin")
            pokemon.add("Ahsoka")
            pokemon.add("Captain Rex")

            collection = mutableListOf<String>("Arya Stark", "Tyrion Lannister", "Euron Greyjoy", "Jon Snow")

            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment = TeamFragment.newInstance(pokemon as ArrayList<String>)

            val fragment2 = CollectionFragment.newInstance(collection as ArrayList<String>)
            fragmentTransaction.add(R.id.team_fragment, fragment)
            fragmentTransaction.add(R.id.collection_fragment, fragment2)
            fragmentTransaction.commit()


        }

    }

}
