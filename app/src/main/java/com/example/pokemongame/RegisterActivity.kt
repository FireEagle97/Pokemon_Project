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
     }
    override fun onStart() {
        super.onStart()
        binding.submit?.setOnClickListener(){
            val intent = Intent(this, TeamActivity::class.java)
            startActivity(intent)
        }
    }

}
