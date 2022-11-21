package com.example.pokemongame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pokemongame.databinding.ActivityMainBinding
import com.example.pokemongame.databinding.ActivityRegisterBinding
import com.example.pokemongame.team_collection.TeamActivity

private lateinit var binding: ActivityRegisterBinding
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.menuBtn?.setOnClickListener{
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }
    }

}
