package com.example.pokemongame

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemongame.databinding.ActivityBattlePhaseBinding
import com.example.pokemongame.databinding.ActivityMainMenuBinding

private lateinit var binding: ActivityBattlePhaseBinding

class BattlePhaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBattlePhaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.runBtn.setOnClickListener{
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }
    }
}