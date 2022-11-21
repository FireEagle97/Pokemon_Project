package com.example.pokemongame

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pokemongame.databinding.ActivityMainBinding
import com.example.pokemongame.databinding.ActivityRegisterBinding
import  com.example.pokemongame.databinding.ActivityMainMenuBinding

private lateinit var binding: ActivityMainMenuBinding

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pokeCenterBtn.setOnClickListener{
//            val intent = Intent(this, PokemonCenterActivity::class.java)
//            startActivity(intent)
            //call a method to reset pp to maxPP and hp to maxHp
            Toast.makeText(applicationContext, "Your pokemon team is ready to battle! ", Toast.LENGTH_SHORT).show()
        }

    }
}