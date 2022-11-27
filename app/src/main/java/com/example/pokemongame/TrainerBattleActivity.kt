package com.example.pokemongame

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.pokemongame.battle.ActivePokemon
import com.example.pokemongame.battle.BattlePhase
import com.example.pokemongame.battle.SelectMovesFragment
import com.example.pokemongame.battle.SwitchingFragment
import com.example.pokemongame.databinding.ActivityTrainerBattleBinding
import com.example.pokemongame.pokemon.*
import java.util.*
import java.util.logging.Logger
import kotlin.collections.ArrayList
import kotlin.math.min
import kotlin.math.pow

private lateinit var binding: ActivityTrainerBattleBinding

class TrainerBattleActivity : AppCompatActivity() {

    companion object{
        val TrainerBattleLog: Logger = Logger.getLogger(TrainerBattleActivity::class.java.name)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrainerBattleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        //Trainer name from intent
        val trainerName = intent.getStringExtra("trainerName").toString()
        //Collection from intent (used for catching)
        var collection = intent.getSerializableExtra("collection") as ArrayList<Pokemon>

        //Initialize Level class for utility
        val levelClass = Level()
        //Initialize fragmentManager for various tasks
        val fragmentManager = supportFragmentManager
        //hold a move position
        var movePositionArray: IntArray = intArrayOf(0)
        //Will hold the position of a yet-to-be switched-in pokemon in the player`s team
        var teamPositionArray: IntArray = intArrayOf(0)
        //Array to trigger a switch and the end of the battle
        var faintedAndEndBattleArray: Array<Boolean> = arrayOf(false, false, false)

        //Determine if it's a trainer battle or wild encounter by receiving the incoming
        //intent holding the boolean (true if in trainer battle, false if not)
        var inTrainerBattle = intent.getBooleanExtra("inTrainerBattle", false)

        //here Generate enemy team (whether the enemy team is a wild team with one pokemon or a trainer's team
        //is determined by the boolean inTrainerBattle above)

        //Testing block
//        val bulbasaur = PokemonCreator().createPokemon(2, "bulbasaur", applicationContext)
//        val charmander2 = PokemonCreator().createPokemon(2,"charmander",applicationContext, "charmander2")
//        levelClass.initializeLevels(bulbasaur, bulbasaur.level, applicationContext)
//        levelClass.initializeLevels(charmander2, charmander2.level, applicationContext)
//        bulbasaur.hp = bulbasaur.maxHp
//        charmander2.hp = charmander2.maxHp
        //Testing block ended

        //Holds the player's team
        var playerTeam =  intent.getSerializableExtra("team") as ArrayList<Pokemon>
        //Holds the the enemy's team. here change it to generated enemy team above
        var enemyTeam = generateOpponentTeam(playerTeam,applicationContext)
        //test enemyTeam
//        TrainerBattleLog.info{
//            "enemyTeam size:${enemyTeam.size}\n"+
//            "pokemon1Level : ${enemyTeam[0].level}\n"+
//                    "defense: ${enemyTeam[0].defense}\n" +
//                    "speed: ${enemyTeam[0].speed}\n" +
//                    "attack: ${enemyTeam[0].attack}\n" +
//                    "specialDefense: ${enemyTeam[0].specialDefense}\n"+
//                    "specialAttack: ${enemyTeam[0].specialAttack}\n"+
//                    "baseStatMaxHp: ${enemyTeam[0].maxHp}\n"+
//                    "experience: ${enemyTeam[0].experience}\n"+
//                    "Hp: ${enemyTeam[0].hp}\n"+
//                    "Level: ${enemyTeam[0].level}\n"+
//                    "moves: ${enemyTeam[0].moves}\n"+
//                    "name: ${enemyTeam[0].name}\n"
//
//        }
        //Init Battle Phase with respective teams for utility and data consistency
        var battlePhase = BattlePhase(playerTeam, enemyTeam)

        //Start of Battle
        BattlePhase.BattleLog.info("Battle Begun!")
//        ActivePokemon for the player
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
//            TrainerBattleLog.info{
//                "intrainer battle: ${inTrainerBattle}\n"
//            }
            playerActivePokemon = ActivePokemon(playerTeam[teamPosition],null, teamPosition,true)
            callBattlePhase(battlePhase, playerActivePokemon, enemyActivePokemon, inTrainerBattle, faintedAndEndBattleArray)
            updateUI(playerActivePokemon,enemyActivePokemon)
            BattlePhase.BattleLog.info("$trainerName's ${playerActivePokemon.pokemon.name} switched in!")
        }

        fragmentManager.setFragmentResultListener("movePosition", this){ requestKey, bundle ->
            //Get the position of the pokemon to be switched-in
            var movePosition = bundle.getInt("movePosition")
            TrainerBattleLog.info{
                "The move Position : $movePosition"
            }
            //Copy the pokemon and Transform the copied pokemon into an ActivePokemon with a null chosenMove and store it in
            playerActivePokemon = ActivePokemon(playerActivePokemon.pokemon, playerActivePokemon.pokemon.moves[movePosition],playerTeam.indexOf(playerActivePokemon.pokemon),true)
            TrainerBattleLog.info{
                "the pokemon: ${playerActivePokemon.pokemon.name}\n"+
                "the pokemon move: ${playerActivePokemon.chosenMove}"
            }
            callBattlePhase(battlePhase, playerActivePokemon, enemyActivePokemon, inTrainerBattle, faintedAndEndBattleArray)
//            playerActivePokemon = ActivePokemon(playerTeam[teamPosition],null, teamPosition,true)
            TrainerBattleLog.info{"if enemy fainted in fight fragment: ${faintedAndEndBattleArray[1]}"}
            updateUI(playerActivePokemon,enemyActivePokemon)
//            BattlePhase.BattleLog.info("$trainerName's ${playerActivePokemon.pokemon.name} switched in!")
            if(!faintedAndEndBattleArray[0]) {
                BattlePhase.BattleLog.info("Turn Begun")

                //here Select a Move (Make buttons visible/invisible. Do not prompt a dialog.
                //I lost too many hours dealing with DialogFragments)
                //For testing purposes, hardcoded the choice
                //            playerActivePokemon.chosenMove = playerActivePokemon.pokemon.moves[0]
                //            enemyActivePokemon.chosenMove = enemyActivePokemon.pokemon.moves[0]

                //here Use an Item logic (make buttons visible/invisible)

                //Switching button OnClickListener. here, callBattlePhase should be called as well

//                TrainerBattleLog.info{"if enemy fainted: ${faintedAndEndBattleArray[1]}"}

                //here Run logic

                //Battle phase call. Also updates faintedAndEndBattleArray (this should be copied and moved in OnClickListeners)


                //If the battle shouldn't end
                if(!faintedAndEndBattleArray[0]){
                    //Force Switch for enemy team if their active pokemon faints
                    if(faintedAndEndBattleArray[1] && !faintedAndEndBattleArray[2]){
                        for(pokemon in enemyTeam){
                            if(pokemon.hp > 0){
                                enemyActivePokemon = ActivePokemon(enemyTeam[enemyTeam.indexOf(pokemon)], null, enemyTeam.indexOf(pokemon), false)
                                BattlePhase.BattleLog.info("Opponent's ${enemyActivePokemon.pokemon.name} switched in!")
                                break
                            }
                        }
                    }

                    //Force Switch for player team if their active pokemon faints
                    if(!faintedAndEndBattleArray[1] && faintedAndEndBattleArray[2]){
                        switch(playerTeam, teamPositionArray, fragmentManager)
                        BattlePhase.BattleLog.info("Opponent's ${playerActivePokemon.pokemon.name} switched in!")
                    }
                }
                BattlePhase.BattleLog.info("Turn Ended")
            } else {
                BattlePhase.BattleLog.info("Battle Ended")
                //Return to main menu
                intent.putExtra("collection", collection as ArrayList<Pokemon>)
                intent.putExtra("team", playerTeam as ArrayList<Pokemon>)
                intent.putExtra("trainerName", trainerName)
                setResult(RESULT_OK, intent)
                finish()
            }
        }


        //here Send out pokemon in index 0 for both teams in the UI
        binding.pokemon1Name.text = playerActivePokemon.pokemon.name
        var pPokemonLevel  = "level: " + playerActivePokemon.pokemon.level
        binding.pokemon1Level.text = pPokemonLevel
        var pPokemon1Hp= "Hp: " +playerActivePokemon.pokemon.hp + "/" + playerActivePokemon.pokemon.maxHp
        binding.pokemon1Hp.text = pPokemon1Hp
        binding.pokemon2Name.text = enemyActivePokemon.pokemon.name
        var ePokemonLevel = "level: " + enemyActivePokemon.pokemon.level
        binding.pokemon2Level.text = ePokemonLevel
        var ePokemon2Hp = "Hp: " + enemyActivePokemon.pokemon.hp + "/" + enemyActivePokemon.pokemon.maxHp
        binding.pokemon2Hp.text = ePokemon2Hp
        binding.pokemon1Img.setImageResource(getPokemonImageResourceId(playerActivePokemon.pokemon.battleStats.species))
        binding.pokemon2Img.setImageResource(getPokemonImageResourceId(enemyActivePokemon.pokemon.battleStats.species))


        //here The buttonProceed is a temporary way to manage the turn order. In reality, all buttons should have
        //OnClickListeners (except Run) that eventually call .playBattleTurn, but before doing so, they should
        //change either the playerActivePokemon or enemyActivePokemon's properties according to the logic of the action
        //If the battle shouldn't end
        binding.switchPokeBtn.setOnClickListener {
            TrainerBattleLog.info{
                "playerTeamSize :${playerTeam.size}"
            }
            switch(playerTeam, teamPositionArray, fragmentManager)
            TrainerBattleLog.info{"if enemy fainted inside switch click: ${faintedAndEndBattleArray[1]}"}

            //call when select poke
//                callBattlePhase(battlePhase, playerActivePokemon, enemyActivePokemon, inTrainerBattle, faintedAndEndBattleArray)
//                updateUI(playerActivePokemon,enemyActivePokemon)
        }
        binding.fightBtn.setOnClickListener{
            //on click open the fragment and do the calls inside the fragment
            selectMove(playerActivePokemon.pokemon.moves, movePositionArray,fragmentManager)
            //call when select move
            faintedAndEndBattleArray = callBattlePhase(battlePhase, playerActivePokemon, enemyActivePokemon, inTrainerBattle, faintedAndEndBattleArray)
            TrainerBattleLog.info{"if enemy fainted in fightbtn: ${faintedAndEndBattleArray[1]}"}


        }

        super.onStart()
    }
    //TO Do put create checkFaintedPokemon method
    //Prompt a DialogFragment and get the index of the incoming switching-in pokemon
    private fun switch(playerTeam: ArrayList<Pokemon>, teamPositionArray: IntArray, fragmentManager: FragmentManager){
        val bundle = Bundle()
        bundle.putSerializable("team", playerTeam)
        bundle.putIntArray("teamPosition", teamPositionArray)
        val switchFragment = SwitchingFragment()
        switchFragment.arguments = bundle
        switchFragment.show(fragmentManager, "fragment")

    }

    private fun selectMove(movesList: ArrayList<Move>, movePositionArray: IntArray, fragmentManager: FragmentManager){
        val bundle = Bundle()
        bundle.putSerializable("moves", movesList)
        bundle.putIntArray("movePosition", movePositionArray)
        val selectMoveFragment = SelectMovesFragment()
        selectMoveFragment.arguments = bundle
        selectMoveFragment.show(fragmentManager, "fragment")
    }

    //Calls the battle phase and updated the faintedAndEndBattleArray
    private fun callBattlePhase(battlePhase: BattlePhase, playerActivePokemon: ActivePokemon, enemyActivePokemon: ActivePokemon,
                                inTrainerBattle: Boolean, faintedAndEndBattleArray: Array<Boolean>) : Array<Boolean> {
        var incomingArray =  battlePhase.playBattlePhase(playerActivePokemon, enemyActivePokemon, inTrainerBattle, applicationContext)
        for(index in 0..2){
            faintedAndEndBattleArray[index] = incomingArray[index]
        }
        return faintedAndEndBattleArray
    }

    //temp code to return a list of random Pokemon
    //will use it to generate the opponent team
    private fun generateOpponentTeam(playerTeam:ArrayList<Pokemon>, context: Context): ArrayList<Pokemon>{
        val randPokemons = Random().nextInt(7);
        var minLevel = playerTeam[0].level
        var maxLevel = playerTeam[0].level
        playerTeam.forEachIndexed{index, pokemon ->
            //get max and min levels in players team
            if(playerTeam[index].level < minLevel){
                minLevel = playerTeam[index].level
            }
            if(playerTeam[index].level > maxLevel){
                maxLevel = playerTeam[index].level
            }
            //assing the hp to max hp

        }
        if(minLevel -5 < 0){
            minLevel = 0
        }else{
            minLevel -=5
        }
        maxLevel += 5
        //assign random moves
        val speciesList : MutableList<String> = mutableListOf("bulbasaur", "charmander", "pidgey")
        val rndPokeList : ArrayList<Pokemon> = arrayListOf()
        for(i in 0..randPokemons){
            val rndSpecies = speciesList[Random().nextInt(speciesList.size)]
            val rndLevel = (minLevel..maxLevel).shuffled().last()

            rndPokeList.add(PokemonCreator().createPokemon(rndLevel,rndSpecies, context))
        }
        //passing moves , hp , experience
        for (pokemon in rndPokeList) {
            Level().initializeLevels(pokemon,pokemon.level,applicationContext)
//            val totalExperience = pokemon.level.toDouble().pow(3.toDouble())
//            Level().addExperience(pokemon,totalExperience,applicationContext)
            pokemon.hp = pokemon.maxHp
//            MoveAssigner().assignNewMoves(pokemon,pokemon.level,applicationContext)
            TrainerBattleLog.info{"initializing enemy pokemon"}
        }

        return rndPokeList
    }
    private fun updateUI(playerActivePokemon: ActivePokemon, enemyActivePokemon: ActivePokemon){
        binding.pokemon1Name.text = playerActivePokemon.pokemon.name
        var pPokemonLevel  = "level: " + playerActivePokemon.pokemon.level
        binding.pokemon1Level.text = pPokemonLevel
        var pPokemon1Hp= "Hp: " +playerActivePokemon.pokemon.hp + "/" + playerActivePokemon.pokemon.maxHp
        binding.pokemon1Hp.text = pPokemon1Hp
        binding.pokemon2Name.text = enemyActivePokemon.pokemon.name
        var ePokemonLevel = "level: " + enemyActivePokemon.pokemon.level
        binding.pokemon2Level.text = ePokemonLevel
        var ePokemon2Hp = "Hp: " + enemyActivePokemon.pokemon.hp + "/" + enemyActivePokemon.pokemon.maxHp
        binding.pokemon2Hp.text = ePokemon2Hp
        binding.pokemon1Img.setImageResource(getPokemonImageResourceId(playerActivePokemon.pokemon.battleStats.species))
        binding.pokemon2Img.setImageResource(getPokemonImageResourceId(enemyActivePokemon.pokemon.battleStats.species))
    }
}