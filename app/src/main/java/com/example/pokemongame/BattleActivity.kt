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
        //Init Level class for utility
        val levelClass = Level()
        //Init fragmentManager for various tasks
        val fragmentManager = supportFragmentManager
        //Will hold the position of a yet-to-be switched-in pokemon in the player`s team
        var teamPositionArray: IntArray = intArrayOf(-1)
        //Array to trigger a switch and end of battle
        var faintedAndEndBattleArray: Array<Boolean> = arrayOf(false, false, false)

        //here Determine if it's a trainer battle or wild encounter by receiving the incoming
        //intent holding the boolean (true if in trainer battle, false if not)
        var inTrainerBattle = false

        //here Generate enemy team (whether the enemy team is a wild team with one pokemon or a trainer's team
        //is determined by the boolean inTrainerBattle above)

        //Testing block
            val charmander = PokemonCreator().createPokemon(15,"charmander",applicationContext)
            val squirtle = PokemonCreator().createPokemon(15, "squirtle", applicationContext)
            val bulbasaur = PokemonCreator().createPokemon(15, "bulbasaur", applicationContext)
            val charmander2 = PokemonCreator().createPokemon(16,"charmander",applicationContext, "charmander2")
            levelClass.initializeLevels(charmander, charmander.level, applicationContext)
            levelClass.initializeLevels(squirtle, squirtle.level, applicationContext)
            levelClass.initializeLevels(bulbasaur, bulbasaur.level, applicationContext)
            levelClass.initializeLevels(charmander2, charmander2.level, applicationContext)
            charmander.hp = charmander.maxHp
            squirtle.hp = squirtle.maxHp
            bulbasaur.hp = bulbasaur.maxHp
            charmander2.hp = charmander2.maxHp
        //Testing block ended

        //Holds the player's team. here change it to incoming intent's team
        var playerTeam =  arrayListOf(squirtle, charmander)
        //Holds the the enemy's team. here change it to generated enemy team above
        var enemyTeam = arrayListOf(bulbasaur, charmander2)
        //Init Battle Phase with respective teams for utility and data consistency
        var battlePhase = Battle_Phase(playerTeam, enemyTeam)

        //Start of Battle
        Battle_Phase.BattleLog.info("Battle Begun!")
        //ActivePokemon for the player
        var playerActivePokemon = ActivePokemon(playerTeam[0], null, 0, true)
        //ActivePokemon for the enemy
        var enemyActivePokemon = ActivePokemon(enemyTeam[0], null, 0, false)

        //Upon the DialogFragment closing, turn the switched-in pokemon into an ActivePokemon stored in the playerActivePokemon var
        //Note: Since The teams get updated in the Battle_Phase class, we just need to copy a pokemon from our team
        fragmentManager.setFragmentResultListener("teamPosition", this){ requestKey, bundle ->
            //Get the position of the pokemon to be switched-in
            var teamPosition = bundle.getInt("teamPosition")
            //Copy the pokemon and Transform the copied pokemon into an ActivePokemon with a null chosenMove and store it in
            //playerActivePokemon var
            playerActivePokemon = ActivePokemon(playerTeam[teamPosition],null, teamPosition,true)
            Battle_Phase.BattleLog.info("Trainer's ${playerActivePokemon.pokemon.name} switched in!")
        }

        //here Send out pokemon in index 0 for both teams in the UI

        //here The buttonProceed is a temporary way to manage the turn order. In reality, all buttons should have
        //OnClickListeners (except Run) that eventually call .playBattleTurn, but before doing so, they should
        //change either the playerActivePokemon or enemyActivePokemon's properties according to the logic of the action
        binding.buttonProceed.setOnClickListener{

            //If the battle shouldn't end
            if(!faintedAndEndBattleArray[0]) {
                Battle_Phase.BattleLog.info("Turn Begun")

                //here Select a Move (Make buttons appear/make them visible on screen. Do not prompt a dialog.
                //I lost too many hours dealing with DialogFragments)
                    //For testing purposes, hardcoded the choice
                    playerActivePokemon.chosenMove = playerActivePokemon.pokemon.moves[0]
                    enemyActivePokemon.chosenMove = enemyActivePokemon.pokemon.moves[0]

                //here Use an Item logic (make buttons visible)

                //Switching button OnClickListener. here, callBattlePhase should be called as well
                binding.button.setOnClickListener {
                    switch(playerTeam, teamPositionArray, fragmentManager)
                }

                //here Run logic

                //Battle phase call. Also updates faintedAndEndBattleArray (this should be copied and moved in OnClickListeners)
                callBattlePhase(battlePhase, playerActivePokemon, enemyActivePokemon, inTrainerBattle, faintedAndEndBattleArray)

                //If the battle shouldn't end
                if(!faintedAndEndBattleArray[0]){
                    //Force Switch for enemy team if their active pokemon faints
                    if(faintedAndEndBattleArray[1] && !faintedAndEndBattleArray[2]){
                        for(pokemon in enemyTeam){
                            if(pokemon.hp > 0){
                                enemyActivePokemon = ActivePokemon(enemyTeam[enemyTeam.indexOf(pokemon)], null, enemyTeam.indexOf(pokemon), false)
                                Battle_Phase.BattleLog.info("Opponent's ${enemyActivePokemon.pokemon.name} switched in!")
                                break
                            }
                        }
                    }

                    //Force Switch for player team if their active pokemon faints
                    if(faintedAndEndBattleArray[1] && faintedAndEndBattleArray[2]){
                        switch(playerTeam, teamPositionArray, fragmentManager)
                    }
                }
                Battle_Phase.BattleLog.info("Turn Ended")
            } else {
                Battle_Phase.BattleLog.info("Battle Ended")
                //here Return to main menu (send back player team with intent)
            }
        }
        super.onStart()
    }

    //Prompt a DialogFragment and get the index of the incoming switching-in pokemon
    private fun switch(playerTeam: ArrayList<Pokemon>, teamPositionArray: IntArray, fragmentManager: FragmentManager){
        val bundle = Bundle()
        bundle.putSerializable("team", playerTeam)
        bundle.putIntArray("teamPosition", teamPositionArray)
        val switchFragment = SwitchingFragment()
        switchFragment.arguments = bundle
        switchFragment.show(fragmentManager, "fragment")
    }

    //Calls the battle phase and updated the faintedAndEndBattleArray
    private fun callBattlePhase(battlePhase: Battle_Phase, playerActivePokemon: ActivePokemon, enemyActivePokemon: ActivePokemon,
                                inTrainerBattle: Boolean, faintedAndEndBattleArray: Array<Boolean>){
        var incomingArray =  battlePhase.playBattlePhase(playerActivePokemon, enemyActivePokemon, inTrainerBattle, applicationContext)
        for(index in 0..2){
            faintedAndEndBattleArray[index] = incomingArray[index]
        }
    }
}