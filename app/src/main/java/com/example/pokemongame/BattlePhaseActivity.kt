package com.example.pokemongame

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.pokemongame.battle.ActivePokemon
import com.example.pokemongame.battle.BattlePhase
import com.example.pokemongame.battle.SelectMovesFragment
import com.example.pokemongame.battle.SwitchingFragment
import com.example.pokemongame.databinding.ActivityBattlePhaseBinding
import com.example.pokemongame.pokemon.*
import java.util.*
import java.util.logging.Logger
import kotlin.collections.ArrayList


class BattlePhaseActivity : AppCompatActivity(), AddMoveDialogFragment.AddMoveDialogListener {
    private lateinit var binding: ActivityBattlePhaseBinding
    private lateinit var collection: ArrayList<Pokemon>
    private lateinit var playerTeam: ArrayList<Pokemon>
    private lateinit var trainerName: String
    private var inTrainerBattle: Boolean = false
    private var numItemUses = 2

    companion object{
        val TrainerBattleLog: Logger = Logger.getLogger(BattlePhaseActivity::class.java.name)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBattlePhaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        //Trainer name from intent
        trainerName = intent.getStringExtra("trainerName").toString()
        //Collection from intent (used for catching)
        collection = intent.getSerializableExtra("collection") as ArrayList<Pokemon>

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
        inTrainerBattle = intent.getBooleanExtra("inTrainerBattle", false)

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
        playerTeam =  intent.getSerializableExtra("team") as ArrayList<Pokemon>
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
        var index = getFirstHealthyPokemonIndex()
        if(index == -1) {
            index = 0
            Toast.makeText(this, "Need to have a pokemon that's not knocked out", Toast.LENGTH_SHORT).show()
            returnToMenu()
        }
        var playerActivePokemon = ActivePokemon(playerTeam[index], null, 0, true)
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
            TrainerBattleLog.info {
                "enemy Moves list size ${enemyActivePokemon.pokemon.moves.size}"
            }
            var enemyMovePosition = Random().nextInt(enemyActivePokemon.pokemon.moves.size)
            TrainerBattleLog.info{
                "The move Position : $movePosition"
            }
            //Copy the pokemon and Transform the copied pokemon into an ActivePokemon with a null chosenMove and store it in
            playerActivePokemon = ActivePokemon(playerActivePokemon.pokemon, playerActivePokemon.pokemon.moves[movePosition],playerTeam.indexOf(playerActivePokemon.pokemon),true)
            enemyActivePokemon = ActivePokemon(enemyActivePokemon.pokemon, enemyActivePokemon.pokemon.moves[enemyMovePosition], enemyTeam.indexOf(enemyActivePokemon.pokemon), false)
            TrainerBattleLog.info{
                "the pokemon: ${playerActivePokemon.pokemon.name}\n"+
                "the pokemon move: ${playerActivePokemon.chosenMove}"
            }
            callBattlePhase(battlePhase, playerActivePokemon, enemyActivePokemon, inTrainerBattle, faintedAndEndBattleArray)
//            playerActivePokemon = ActivePokemon(playerTeam[teamPosition],null, teamPosition,true)
            TrainerBattleLog.info{"if enemy fainted in fight fragment: ${faintedAndEndBattleArray[1]}"}
            TrainerBattleLog.info{"if player fainted in fight fragment: ${faintedAndEndBattleArray[2]}"}
            updateUI(playerActivePokemon,enemyActivePokemon)
//            BattlePhase.BattleLog.info("$trainerName's ${playerActivePokemon.pokemon.name} switched in!")
            if(!faintedAndEndBattleArray[0]) {
                BattlePhase.BattleLog.info("Turn Begun")

                //If the battle shouldn't end
                if(!faintedAndEndBattleArray[0]){
                    //Force Switch for enemy team if their active pokemon faints
                    if(faintedAndEndBattleArray[1] && !faintedAndEndBattleArray[2]){
                        for(pokemon in enemyTeam){
                            if(pokemon.hp > 0){
                                enemyActivePokemon = ActivePokemon(enemyTeam[enemyTeam.indexOf(pokemon)], null, enemyTeam.indexOf(pokemon), false)
                                BattlePhase.BattleLog.info("Opponent's ${enemyActivePokemon.pokemon.name} switched in!")
                                updateUI(playerActivePokemon,enemyActivePokemon)
                                break
                            }
                        }
                    }

                    //Force Switch for player team if their active pokemon faints
                    if(faintedAndEndBattleArray[1] && faintedAndEndBattleArray[2]){
                        switch(playerTeam, teamPositionArray, fragmentManager)
                        updateUI(playerActivePokemon,enemyActivePokemon)
                        BattlePhase.BattleLog.info("player's ${playerActivePokemon.pokemon.name} switched in!")
                    }
                }
                BattlePhase.BattleLog.info("Turn Ended")
            } else {
                BattlePhase.BattleLog.info("Battle Ended")
                //Return to main menu
                returnToMenu()
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
        }

        binding.itemBtn.setOnClickListener(){
            if(numItemUses > 0) {
                binding.potion.visibility = View.VISIBLE
                if (!inTrainerBattle) {
                    binding.capture.visibility = View.VISIBLE

                }
            }
            else{
                Toast.makeText(this, "You have reached the maximum number of item uses", Toast.LENGTH_SHORT).show()
            }
        }
        binding.potion.setOnClickListener(){
            if(playerActivePokemon.pokemon.hp + 20 < playerActivePokemon.pokemon.maxHp){
                playerActivePokemon.pokemon.hp += 20
            }
            else{
                playerActivePokemon.pokemon.hp = playerActivePokemon.pokemon.maxHp
            }
            updateUI(playerActivePokemon,enemyActivePokemon)
            hideItems()
            numItemUses--
        }
        binding.capture.setOnClickListener(){
            var chanceToCapture = 1-(enemyActivePokemon.pokemon.hp / enemyActivePokemon.pokemon.maxHp)
            if(chanceToCapture > Random().nextDouble()){
                collection.add(enemyActivePokemon.pokemon)
                Toast.makeText(this, enemyActivePokemon.pokemon.name + " has been captured and added to the team", Toast.LENGTH_SHORT).show()
                returnToMenu()
            }
            else{
                Toast.makeText(this, "could not capture pokemon, returning to battle", Toast.LENGTH_SHORT).show()
            }
            hideItems()
            numItemUses--
        }


        //returns to menu
        binding.runBtn.setOnClickListener(){
//            returnToMenu();
            //for testing purposes
            showAddMoveDialog()
        }
        super.onStart()
    }
    private fun returnToMenu(){
        intent.putExtra("collection", collection as ArrayList<Pokemon>)
        intent.putExtra("team", playerTeam as ArrayList<Pokemon>)
        intent.putExtra("trainerName", trainerName)
        setResult(RESULT_OK, intent)
        finish()
    }
    private fun hideItems() {
        binding.potion.visibility = View.GONE
        binding.capture.visibility = View.GONE
    }
    private fun getFirstHealthyPokemonIndex(): Int{
        for(i in 0 until playerTeam.size){
            if(playerTeam[i].hp > 0){
                return i
            }
        }
        return -1
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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateOpponentTeam(playerTeam:ArrayList<Pokemon>, context: Context): ArrayList<Pokemon>{

        var randPokemons = 0 // if not in trainer battle.
        if(inTrainerBattle){
            randPokemons = Random().nextInt(6); // if in trainer battle.
        }
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
        if(minLevel -5 <= 0){
            minLevel = 1
        }else{
            minLevel -=5
        }
        maxLevel += 5
        //assign random moves
        val speciesList : MutableList<String> = mutableListOf("bulbasaur", "charmander", "squirtle")
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
    private fun showAddMoveDialog() {
        // Create an instance of the dialog fragment and show it
        val dialog = AddMoveDialogFragment()
        dialog.show(supportFragmentManager, "AddMoveDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        //for testing
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        // User touched the dialog's negative button
    }


}