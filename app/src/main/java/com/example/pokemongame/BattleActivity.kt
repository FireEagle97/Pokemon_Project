package com.example.pokemongame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.pokemongame.battle.ActivePokemon
import com.example.pokemongame.battle.Battle_Phase
import com.example.pokemongame.battle.SwitchingFragment
import com.example.pokemongame.databinding.BattleActivityBinding
import com.example.pokemongame.pokemon.Level
import com.example.pokemongame.pokemon.Move
import com.example.pokemongame.pokemon.Pokemon
import com.example.pokemongame.pokemon.PokemonCreator

private lateinit var binding: BattleActivityBinding

class BattleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = BattleActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        //Init Level class
        val levelClass = Level()
        //Init fragmentManager
        val fragmentManager = supportFragmentManager
        //var to hold the future position of a switched pokemon in the player`s team
        var teamPositionArray: Array<Int> = arrayOf(-1)
        //Array to trigger a switch and end of battle
        var faintedAndEndBattleArray: Array<Boolean> = arrayOf(false, false)


        val charmander = PokemonCreator().createPokemon(15,"charmander",applicationContext)
        val squirtle = PokemonCreator().createPokemon(15, "squirtle", applicationContext)
        val bulbasaur = PokemonCreator().createPokemon(15, "bulbasaur", applicationContext)
        levelClass.initializeLevels(charmander, charmander.level, applicationContext)
        levelClass.initializeLevels(squirtle, squirtle.level, applicationContext)
        levelClass.initializeLevels(bulbasaur, bulbasaur.level, applicationContext)
        //var to hold the team
        var playerTeam =  arrayListOf(squirtle, charmander)
        //var to hold the enemy team
        var enemyTeam = arrayListOf(bulbasaur)
        //Init Battle Phase
        var battlePhase = Battle_Phase(playerTeam, enemyTeam)

        //Test
        Battle_Phase.BattleLog.info(playerTeam[0].hp.toString() + " " + playerTeam[1].hp.toString())

        //Switch logic
        binding.button.setOnClickListener {
            switch(playerTeam, enemyTeam, fragmentManager)
        }

        //Start of Battle
        //ActivePokemon for the player
        var playerActivePokemon = ActivePokemon(playerTeam[0], null, 0, true)
        //ActivePokemon for the enemy
        var enemyActivePokemon = ActivePokemon(enemyTeam[0], null, 0, false)

        //Boolean to let us continue the battle (For testing)
        var proceed = false;

        fragmentManager.setFragmentResultListener("teamPosition", this){ requestKey, bundle ->
            //Get the position of the pokemon to be switch
            teamPositionArray = bundle.getSerializable("teamPosition") as Array<Int>
            var teamPosition = teamPositionArray[0]
            //Copy the pokemon
            //Transform the copied pokemon into an ActivePokemon with a null chosenMove and shove it in .playBattleTurn
            playerActivePokemon = ActivePokemon(playerTeam[teamPosition],null, teamPosition,true)
            Battle_Phase.BattleLog.info(playerActivePokemon.pokemon.name + " switched in!")
            proceed = true
        }

        binding.buttonProceed.setOnClickListener{
            proceed = true
        }

            //Send out pokemon in index 0 for both teams here


        //Loop until battle ends
        while(!faintedAndEndBattleArray[0]) {
            proceed = false

            //Select a Move logic
                //For testing purposes
                playerActivePokemon.chosenMove = playerActivePokemon.pokemon.moves[0]
                enemyActivePokemon.chosenMove = enemyActivePokemon.pokemon.moves[0]
            //Use an Item logic

            //Run logic

            //Battle phase if proceed is true
            if(proceed) {
                faintedAndEndBattleArray = battlePhase.playBattleTurn(
                    playerActivePokemon,
                    enemyActivePokemon, false, applicationContext
                )
            }
        }
        //Return to main menu (send back player team)
        super.onStart()
    }

    private fun switch(playerTeam: ArrayList<Pokemon>, teamPositionArray: ArrayList<Pokemon>, fragmentManager: FragmentManager){
        val bundle = Bundle()
        bundle.putSerializable("team", playerTeam)
        bundle.putSerializable("teamPosition", teamPositionArray)
        val switchFragment = SwitchingFragment()
        switchFragment.arguments = bundle
        switchFragment.show(fragmentManager, "fragment")
    }

}