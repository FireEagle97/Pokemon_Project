package com.example.pokemongame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemongame.databinding.ActivityRegisterBinding
import com.example.pokemongame.pokemon.Pokemon
import com.example.pokemongame.pokemon.PokemonCreator
import android.widget.RadioButton





private lateinit var binding: ActivityRegisterBinding
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Three default pokemon for the user to choose from
        binding.pokemon1.text = buildString {
        append("Bulbasaur (lvl 5)")
    }
        binding.pokemon1.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.bulbasaur,0)
        binding.pokemon2.text =buildString {
            append("Squirtle (lvl 5)")
        }
        binding.pokemon2.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.squirtle,0)
        binding.pokemon3.text =buildString {
            append("Charmander (lvl 5)")
        }
        binding.pokemon3.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.charmander,0)



        val team = ArrayList<Pokemon>()
        //when a pokemon is selected, allow user to select name and submit
        binding.firstChoice.setOnCheckedChangeListener { group, checkedId -> // I don't know if there is a way to remove these params
                binding.pokemonNameQuery?.visibility = View.VISIBLE
                binding.pokemonNameInput?.visibility = View.VISIBLE
                binding.menuBtn?.visibility = View.VISIBLE
            }
        binding.menuBtn?.setOnClickListener(){
                if(binding.inputName.text.isNotBlank()){
                    

                    }
            else{
                binding.mustEnterName?.visibility  = View.VISIBLE
                }
        }




//
//        binding.menuBtn?.setOnClickListener{
//            val intent = Intent(this, MainMenuActivity::class.java)
//            startActivity(intent)
        //}
    }

}
