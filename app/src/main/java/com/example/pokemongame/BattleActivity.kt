package com.example.pokemongame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemongame.databinding.BattleActivityBinding

class BattleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: BattleActivityBinding = BattleActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}