package com.example.pokemongame

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
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


class BattlePhaseActivity : AppCompatActivity(), AddMoveDialogFragment.AddMoveDialogListener{
    private lateinit var binding: ActivityBattlePhaseBinding
    private lateinit var collection: ArrayList<Pokemon>
    private lateinit var playerTeam: ArrayList<Pokemon>
    private lateinit var trainerName: String
    private var inTrainerBattle: Boolean = false
    private var numItemUses = 2
    //Initialize fragmentManager for various tasks
    val fragmentManager = supportFragmentManager
    //Initialize Level class for utility to level up the PLAYER's pokemon
    val levelClass = Level(fragmentManager)
    //new Move holder for player
    lateinit var newMove: Move
    //pokemon that will learn a new move
    lateinit var pokemonToLearnMove: Pokemon
    //Enemy team
    lateinit var enemyTeam: ArrayList<Pokemon>
    //Will hold the position of a yet-to-be switched-in pokemon in the player`s team
    lateinit var teamPositionArray: IntArray

    lateinit var playerActivePokemon: ActivePokemon

    lateinit var enemyActivePokemon: ActivePokemon

    var battleTextValue: Array<String> = arrayOf("")

    var pace: Array<Boolean> = arrayOf(false)

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

        //hold a move position
        var movePositionArray: IntArray = intArrayOf(0)
        //Initialize teamPositionArray
        teamPositionArray = intArrayOf(0)
        //Array to trigger a switch and the end of the battle
        var faintedAndEndBattleArray: Array<Boolean> = arrayOf(false, false, false)

        //Determine if it's a trainer battle or wild encounter by receiving the incoming
        //intent holding the boolean (true if in trainer battle, false if not)
        inTrainerBattle = intent.getBooleanExtra("inTrainerBattle", false)

        //here Generate enemy team (whether the enemy team is a wild team with one pokemon or a trainer's team
        //is determined by the boolean inTrainerBattle above)


        //Holds the player's team
        playerTeam =  intent.getSerializableExtra("team") as ArrayList<Pokemon>
        //Holds the the enemy's team. It is changed to a generated enemy team above
        enemyTeam = generateOpponentTeam(playerTeam,applicationContext)

        //Init Battle Phase with respective teams for utility and data consistency
        val battlePhase = BattlePhase(playerTeam, enemyTeam, fragmentManager, battleTextValue, pace, this)

        //Start of Battle
        BattlePhase.BattleLog.info("Battle Begun!")
//        ActivePokemon for the player
        var index = getFirstHealthyPokemonIndex()
        if(index == -1) {
            index = 0
            Toast.makeText(this, "Need to have a pokemon that's not knocked out", Toast.LENGTH_SHORT).show()
            returnToMenu()
        }
        playerActivePokemon = ActivePokemon(playerTeam[index], null, 0, true)
        //ActivePokemon for the enemy
        enemyActivePokemon = ActivePokemon(enemyTeam[0], null, 0, false)

        //here Send out pokemon in index 0 for both teams in the UI
        binding.pokemon1Name.text = playerActivePokemon.pokemon.name
        var pPokemonLevel  = "level: " + playerActivePokemon.pokemon.level
        binding.pokemon1Level.text = pPokemonLevel
        var pPokemon1Hp= "Hp: " +playerActivePokemon.pokemon.hp + "/" + playerActivePokemon.pokemon.maxHp
        binding.pokemon1Hp.text = pPokemon1Hp
        binding.pokemon1Img.setImageResource(getPokemonImageResourceId(playerActivePokemon.pokemon.battleStats.species))

        //If we are in a trainer battle, print a "sent out" message, else print a "appeared" message
        val adversary: String
        var initialText: String = ""
        if(inTrainerBattle){
            adversary = "opponent's"
            TrainerBattleLog.info("The $adversary ${enemyActivePokemon.pokemon.name} was sent out!")
            initialText = "The $adversary ${enemyActivePokemon.pokemon.name} was sent out!"
        } else {
            adversary = "wild"
            TrainerBattleLog.info("A $adversary ${enemyActivePokemon.pokemon.name} appeared!")
            initialText = "A $adversary ${enemyActivePokemon.pokemon.name} appeared!"
        }
        showBattleText(initialText,"stop")

        binding.pokemon2Name.text = enemyActivePokemon.pokemon.name
        var ePokemonLevel = "level: " + enemyActivePokemon.pokemon.level
        binding.pokemon2Level.text = ePokemonLevel
        var ePokemon2Hp = "Hp: " + enemyActivePokemon.pokemon.hp + "/" + enemyActivePokemon.pokemon.maxHp
        binding.pokemon2Hp.text = ePokemon2Hp
        binding.pokemon2Img.setImageResource(getPokemonImageResourceId(enemyActivePokemon.pokemon.battleStats.species))
        //Hide pokemon2's data
        binding.pokemon2Name.visibility = INVISIBLE
        binding.pokemon2Level.visibility = INVISIBLE
        binding.pokemon2Hp.visibility = INVISIBLE
        binding.pokemon2Img.visibility = INVISIBLE

        //Fragment listener for new moves
        fragmentManager.setFragmentResultListener("newMove", this){ _, bundle ->
            newMove = bundle.getSerializable("newMove") as Move
            pokemonToLearnMove = bundle.getSerializable("pokemon") as Pokemon
            val decision = bundle.getBoolean("decision")

            if(decision) {
                MoveAssigner(fragmentManager).addMoveOrSummonFragment(pokemonToLearnMove, newMove)
            }
        }

        fragmentManager.setFragmentResultListener("newMovePosition", this) { _,bundle ->
            val movePosition = bundle.getInt("movePosition")
            TrainerBattleLog.info(movePosition.toString())
            MoveAssigner().replaceMove(pokemonToLearnMove, newMove, movePosition)
        }

        //Upon the DialogFragment closing, turn the switched-in pokemon into an ActivePokemon stored in the playerActivePokemon var
        //Note: Since The teams get updated in the Battle_Phase class, we just need to copy a pokemon from our team
        fragmentManager.setFragmentResultListener("teamPosition", this){ requestKey, bundle ->
            //Get the position of the pokemon to be switched-in
            val teamPosition = bundle.getInt("teamPosition")
            //Copy the pokemon and Transform the copied pokemon into an ActivePokemon with a null chosenMove and store it in
            //playerActivePokemon var
//            TrainerBattleLog.info{
//                "intrainer battle: ${inTrainerBattle}\n"
//            }
            playerActivePokemon = ActivePokemon(playerTeam[teamPosition],null, teamPosition,true)
            callBattlePhase(battlePhase, inTrainerBattle, faintedAndEndBattleArray)
            updateUI(playerActivePokemon,enemyActivePokemon)
            BattlePhase.BattleLog.info("$trainerName's ${playerActivePokemon.pokemon.name} switched in!")
        }

        //Choose a move
        fragmentManager.setFragmentResultListener("movePosition", this){ requestKey, bundle ->
            //Get the position of the pokemon to be switched-in
            val movePosition = bundle.getInt("movePosition")
//            TrainerBattleLog.info {
//                "enemy Moves list size ${enemyActivePokemon.pokemon.moves.size}"
//            }
            val enemyMovePosition = Random().nextInt(enemyActivePokemon.pokemon.moves.size)
//            TrainerBattleLog.info{
//                "The move Position : $movePosition"
//            }
            //Copy the pokemon and Transform the copied pokemon into an ActivePokemon with a null chosenMove and store it in
            playerActivePokemon = ActivePokemon(playerActivePokemon.pokemon, playerActivePokemon.pokemon.moves[movePosition],playerTeam.indexOf(playerActivePokemon.pokemon),true)
            enemyActivePokemon = ActivePokemon(enemyActivePokemon.pokemon, enemyActivePokemon.pokemon.moves[enemyMovePosition], enemyTeam.indexOf(enemyActivePokemon.pokemon), false)
//            TrainerBattleLog.info{
//                "the pokemon: ${playerActivePokemon.pokemon.name}\n"+
//                "the pokemon move: ${playerActivePokemon.chosenMove}"
//            }
            callBattlePhase(battlePhase, inTrainerBattle, faintedAndEndBattleArray)
//            playerActivePokemon = ActivePokemon(playerTeam[teamPosition],null, teamPosition,true)
//            TrainerBattleLog.info{"if enemy fainted in fight fragment: ${faintedAndEndBattleArray[1]}"}
//            TrainerBattleLog.info{"if player fainted in fight fragment: ${faintedAndEndBattleArray[2]}"}
            updateUI(playerActivePokemon,enemyActivePokemon)
//            BattlePhase.BattleLog.info("$trainerName's ${playerActivePokemon.pokemon.name} switched in!")

        }

        //here The buttonProceed is a temporary way to manage the turn order. In reality, all buttons should have
        //OnClickListeners (except Run) that eventually call .playBattleTurn, but before doing so, they should
        //change either the playerActivePokemon or enemyActivePokemon's properties according to the logic of the action
        //If the battle shouldn't end
        binding.switchPokeBtn.setOnClickListener {
            var oneRemaining = false
            var counter = 0
            for(pokemon in playerTeam){
                if (pokemon.hp == 0){
                    counter++
                }
            }
            if(counter == playerTeam.count()-1){
                oneRemaining = true
            }
            if(playerTeam.size == 1 || oneRemaining){
                Toast.makeText(applicationContext, "You cannot switch with one pokemon left!", Toast.LENGTH_SHORT).show()
            } else {
                switch(playerTeam, teamPositionArray, fragmentManager)
            }
        }
        binding.fightBtn.setOnClickListener{
            //on click open the fragment and do the calls inside the fragment
            selectMove(playerActivePokemon.pokemon.moves, movePositionArray,fragmentManager)
            //call when select move
        }

        binding.itemBtn.setOnClickListener{
            if(numItemUses > 0) {
                binding.potion.visibility = VISIBLE
                if (!inTrainerBattle) {
                    binding.capture.visibility = VISIBLE

                }
            }
            else{
                Toast.makeText(this, "You have reached the maximum number of item uses", Toast.LENGTH_SHORT).show()
            }
        }
        binding.potion.setOnClickListener{
            if(playerActivePokemon.pokemon.hp + 20 < playerActivePokemon.pokemon.maxHp){
                playerActivePokemon.pokemon.hp += 20
            }
            else{
                playerActivePokemon.pokemon.hp = playerActivePokemon.pokemon.maxHp
            }
            TrainerBattleLog.info("$trainerName used a potion!")
            updateUI(playerActivePokemon,enemyActivePokemon)
            hideItems()
            numItemUses--
        }
        binding.capture.setOnClickListener{
            val chanceToCapture = 1-(enemyActivePokemon.pokemon.hp / enemyActivePokemon.pokemon.maxHp)
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
        binding.runBtn.setOnClickListener{
//            returnToMenu();
            //for testing purposes
            //showAddMoveDialog()
            //levelClass.levelUp(playerTeam[0], this)
        }
        super.onStart()
    }
    private fun returnToMenu(){
        intent.putExtra("collection", collection)
        intent.putExtra("team", playerTeam)
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
        val selectMoveFragment = SelectMovesFragment(true)
        selectMoveFragment.arguments = bundle
        selectMoveFragment.show(fragmentManager, "fragment")
    }


    //Calls the battle phase and updated the faintedAndEndBattleArray
    private fun callBattlePhase(battlePhase: BattlePhase,
                                inTrainerBattle: Boolean, faintedAndEndBattleArray: Array<Boolean>) : Array<Boolean> {

        BattlePhase.BattleLog.info("Turn Begun")

        val incomingArray =  battlePhase.playBattlePhase(playerActivePokemon, enemyActivePokemon, inTrainerBattle, applicationContext)
        for(index in 0..2){
            faintedAndEndBattleArray[index] = incomingArray[index]
        }

            //If the battle shouldn't end
            if(!faintedAndEndBattleArray[0]){
                //Force Switch enemy team if their active pokemon faints
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

                //Force Switch player team if their active pokemon faints
                if(faintedAndEndBattleArray[1] && faintedAndEndBattleArray[2]){
                    switch(playerTeam, teamPositionArray, fragmentManager)
                    updateUI(playerActivePokemon,enemyActivePokemon)
                    BattlePhase.BattleLog.info("$trainerName's ${playerActivePokemon.pokemon.name} switched in!")
                }
            } else {
            BattlePhase.BattleLog.info("Battle Ended")
            //Return to main menu
            returnToMenu()
        }
        BattlePhase.BattleLog.info("Turn Ended")
        return faintedAndEndBattleArray
    }

    //temp code to return a list of random Pokemon
    //will use it to generate the opponent team
    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateOpponentTeam(playerTeam:ArrayList<Pokemon>, context: Context): ArrayList<Pokemon>{

        var randPokemons = 0 // if not in trainer battle.
        if(inTrainerBattle){
            randPokemons = Random().nextInt(6) // if in trainer battle.
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
            pokemon.hp = pokemon.maxHp
            TrainerBattleLog.info{"initializing enemy pokemon"}
        }

        return rndPokeList
    }
    private fun updateUI(playerActivePokemon: ActivePokemon, enemyActivePokemon: ActivePokemon){
        binding.pokemon1Name.text = playerActivePokemon.pokemon.name
        val pPokemonLevel  = "level: " + playerActivePokemon.pokemon.level
        binding.pokemon1Level.text = pPokemonLevel
        val pPokemon1Hp= "Hp: " +playerActivePokemon.pokemon.hp + "/" + playerActivePokemon.pokemon.maxHp
        binding.pokemon1Hp.text = pPokemon1Hp
        binding.pokemon2Name.text = enemyActivePokemon.pokemon.name
        val ePokemonLevel = "level: " + enemyActivePokemon.pokemon.level
        binding.pokemon2Level.text = ePokemonLevel
        val ePokemon2Hp = "Hp: " + enemyActivePokemon.pokemon.hp + "/" + enemyActivePokemon.pokemon.maxHp
        binding.pokemon2Hp.text = ePokemon2Hp
        binding.pokemon1Img.setImageResource(getPokemonImageResourceId(playerActivePokemon.pokemon.battleStats.species))
        binding.pokemon2Img.setImageResource(getPokemonImageResourceId(enemyActivePokemon.pokemon.battleStats.species))
    }

    fun showBattleText(text: String, hint: String){
        val buttons = binding.buttons
        val battleText = binding.battleText
        battleText.visibility = VISIBLE
        battleText.hint = hint
        battleText.text = text
        buttons.visibility = INVISIBLE
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        //positive button
        TrainerBattleLog.info("Trainer chose yes")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        // User touched the dialog's negative button
        TrainerBattleLog.info("Trainer chose no")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        binding.battleText.visibility = INVISIBLE
        if(binding.battleText.hint == "stop"){
            //buttons visibility
            binding.buttons.visibility = VISIBLE
            //enemy pokemon visibility
            binding.pokemon2Name.visibility = VISIBLE
            binding.pokemon2Level.visibility = VISIBLE
            binding.pokemon2Hp.visibility = VISIBLE
            binding.pokemon2Img.visibility = VISIBLE
            //player pokemon visibility
            binding.pokemon1Name.visibility = VISIBLE
            binding.pokemon1Level.visibility = VISIBLE
            binding.pokemon1Hp.visibility = VISIBLE
            binding.pokemon1Img.visibility = VISIBLE
        } else {
            pace[0] = true
        }
        return super.onTouchEvent(event)
    }
}