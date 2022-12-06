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
import com.example.pokemongame.battle.*
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
    //Will hold the player's active pokemon
    lateinit var playerActivePokemon: ActivePokemon
    //Will hold the opponent's active pokemon
    lateinit var enemyActivePokemon: ActivePokemon
    //Holds all text to be displayed in the in-game battle log
    var battleTextList: MutableList<BattleText> = mutableListOf()
    //Boolean that determines if the battle is over (to return to main menu)
    var endBattle: Boolean = false
    //Array of Boolean used to force the user to switch
    var forcePlayerSwitch: Array<Boolean> = arrayOf(false)
    //Boolean used to force the enemy to switch
    var forceEnemySwitch = false
    //Array to trigger a switch and the end of the battle
    var faintedAndEndBattleArray: Array<Boolean> = arrayOf(false, false, false)
    //Double array to hold the experience gained by the pokemon that hasn't fainted
    var gainedExperience: Array<Double> = arrayOf(0.0)

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

        //Determine if it's a trainer battle or wild encounter by receiving the incoming
        //intent holding the boolean (true if in trainer battle, false if not)
        inTrainerBattle = intent.getBooleanExtra("inTrainerBattle", false)

        //Holds the player's team
        playerTeam =  intent.getSerializableExtra("team") as ArrayList<Pokemon>
        //Testing. Add a poke to the player's team
        var charm = PokemonCreator().createPokemon(5, "charmander", this, "Charm")
        Level().initializeLevels(charm, charm.level, this)
        charm.hp = charm.maxHp
        playerTeam.add(charm)
        //Holds the the enemy's team. It is changed to a generated enemy team above
        enemyTeam = generateOpponentTeam(playerTeam,applicationContext)

        //Init Battle Phase with respective teams for utility and data consistency
        val battlePhase = BattlePhase(playerTeam, enemyTeam, fragmentManager,this, trainerName, gainedExperience)

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

        //Send out pokemon in index 0 for both teams in the UI
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
            adversary = "Opponent's"
            TrainerBattleLog.info("$adversary ${enemyActivePokemon.pokemon.name} was sent out!")
            initialText = "$adversary ${enemyActivePokemon.pokemon.name} was sent out!"
        } else {
            adversary = "wild"
            TrainerBattleLog.info("A $adversary ${enemyActivePokemon.pokemon.name} appeared!")
            initialText = "A $adversary ${enemyActivePokemon.pokemon.name} appeared!"
        }
        showBattleText(initialText)

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

        //Fragment listener for new moves prompt
        fragmentManager.setFragmentResultListener("newMove", this){ _, bundle ->
            newMove = bundle.getSerializable("newMove") as Move
            pokemonToLearnMove = bundle.getSerializable("pokemon") as Pokemon
            val decision = bundle.getBoolean("decision")

            if(decision) {
                MoveAssigner(fragmentManager).addMoveOrSummonFragment(pokemonToLearnMove, newMove)
            }
        }

        //Fragment listener for move replacement
        fragmentManager.setFragmentResultListener("newMovePosition", this) { _,bundle ->
            val movePosition = bundle.getInt("movePosition")
            TrainerBattleLog.info(movePosition.toString())
            MoveAssigner().replaceMove(pokemonToLearnMove, newMove, movePosition)
        }

        //Upon the Switch DialogFragment closing, turn the switched-in pokemon into an ActivePokemon
        //Note: Since The teams get updated in the Battle_Phase class, we just need to copy a pokemon from our team
        fragmentManager.setFragmentResultListener("teamPosition", this){ requestKey, bundle ->
            //Get the position of the pokemon to be switched-in
            val teamPosition = bundle.getInt("teamPosition")
            //Copy the pokemon and Transform the copied pokemon into an ActivePokemon with a null chosenMove and store it in
            showBattleText("$trainerName's ${playerActivePokemon.pokemon.name} was withdrawn!")
            playerActivePokemon = ActivePokemon(playerTeam[teamPosition],null, teamPosition,true)

            binding.pokemon1Name.text = playerActivePokemon.pokemon.name
            val pPokemonLevel  = "level: " + playerActivePokemon.pokemon.level
            binding.pokemon1Level.text = pPokemonLevel
            val pPokemon1Hp= "Hp: " +playerActivePokemon.pokemon.hp + "/" + playerActivePokemon.pokemon.maxHp
            binding.pokemon1Hp.text = pPokemon1Hp
            binding.pokemon1Img.setImageResource(getPokemonImageResourceId(playerActivePokemon.pokemon.battleStats.species))
            if(!forcePlayerSwitch[0]) {
                //Choose a random move for the enemy
                val enemyMovePosition = Random().nextInt(enemyActivePokemon.pokemon.moves.size)
                enemyActivePokemon.chosenMove = enemyActivePokemon.pokemon.moves[enemyMovePosition]
                callBattlePhase(battlePhase, inTrainerBattle, faintedAndEndBattleArray)
            }
            forcePlayerSwitch[0] = false
            showBattleText("$trainerName's ${playerActivePokemon.pokemon.name} switched in!")

        }

        //Choose a move
        fragmentManager.setFragmentResultListener("movePosition", this){ requestKey, bundle ->
            //Get the position of the pokemon to be switched-in
            val movePosition = bundle.getInt("movePosition")

            val enemyMovePosition = Random().nextInt(enemyActivePokemon.pokemon.moves.size)

            //Copy the pokemon and Transform the copied pokemon into an ActivePokemon with a null chosenMove and store it in
            playerActivePokemon.chosenMove = playerActivePokemon.pokemon.moves[movePosition]
            enemyActivePokemon.chosenMove = enemyActivePokemon.pokemon.moves[enemyMovePosition]

            callBattlePhase(battlePhase, inTrainerBattle, faintedAndEndBattleArray)

        }

        //Check if we can switch, if we can, call switch()
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

                binding.potion.visibility = VISIBLE
                if (!inTrainerBattle) {
                    binding.capture.visibility = VISIBLE

                }
        }
        binding.potion.setOnClickListener{
            if(playerActivePokemon.pokemon.hp + 20 < playerActivePokemon.pokemon.maxHp){
                playerActivePokemon.pokemon.hp += 20
            }
            else{
                playerActivePokemon.pokemon.hp = playerActivePokemon.pokemon.maxHp
            }
            val enemyMovePosition = Random().nextInt(enemyActivePokemon.pokemon.moves.size)
            enemyActivePokemon.chosenMove = enemyActivePokemon.pokemon.moves[enemyMovePosition]
            playerActivePokemon.chosenMove = null
            callBattlePhase(battlePhase, inTrainerBattle, faintedAndEndBattleArray)
            TrainerBattleLog.info("$trainerName healed their pokemon by using a potion!")
            addStringToBattleTextList("$trainerName healed their pokemon by using a potion!", true, true, false)
            hideItems()
        }
        binding.capture.setOnClickListener{
            playerActivePokemon.chosenMove = null
            enemyActivePokemon.chosenMove = null
            val chanceToCapture = (1.0- (enemyActivePokemon.pokemon.hp.toDouble() / enemyActivePokemon.pokemon.maxHp.toDouble()))
            if(chanceToCapture > Random().nextDouble()){
                collection.add(enemyActivePokemon.pokemon)
                TrainerBattleLog.info("$trainerName  successfully captured the enemy's pokemon!")
                addStringToBattleTextList("$trainerName successfully captured the enemy's pokemon!")
                
                endBattle = true
            }
            else{
                TrainerBattleLog.info("$trainerName did not manage to capture the enemy's pokemon!")
                addStringToBattleTextList("$trainerName did not manage to capture the enemy's pokemon!")
                callBattlePhase(battlePhase, inTrainerBattle, faintedAndEndBattleArray)

            }
            val enemyMovePosition = Random().nextInt(enemyActivePokemon.pokemon.moves.size)
            enemyActivePokemon.chosenMove = enemyActivePokemon.pokemon.moves[enemyMovePosition]
            hideItems()
        }

        //returns to menu
        binding.runBtn.setOnClickListener{
            returnToMenu()

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

    //Prompt a DialogFragment and get the index of the incoming switching-in pokemon
    private fun switch(playerTeam: ArrayList<Pokemon>, teamPositionArray: IntArray, fragmentManager: FragmentManager){
        val bundle = Bundle()
        bundle.putSerializable("team", playerTeam)
        bundle.putIntArray("teamPosition", teamPositionArray)
        val switchFragment = SwitchingFragment()
        switchFragment.isCancelable = false
        switchFragment.arguments = bundle
        switchFragment.show(fragmentManager, "fragment")
    }

    private fun selectMove(movesList: ArrayList<Move>, movePositionArray: IntArray, fragmentManager: FragmentManager){
        val bundle = Bundle()
        bundle.putSerializable("moves", movesList)
        bundle.putIntArray("movePosition", movePositionArray)
        val selectMoveFragment = SelectMovesFragment(true)
        selectMoveFragment.isCancelable = false
        selectMoveFragment.arguments = bundle
        selectMoveFragment.show(fragmentManager, "fragment")
    }


    //Calls the battle phase and updates the faintedAndEndBattleArray
    private fun callBattlePhase(battlePhase: BattlePhase,
                                inTrainerBattle: Boolean, faintedAndEndBattleArray: Array<Boolean>) : Array<Boolean> {

        BattlePhase.BattleLog.info("Turn Begun")
        showBattleText("Turn Begun")

        val incomingArray =  battlePhase.playBattlePhase(playerActivePokemon, enemyActivePokemon, inTrainerBattle, applicationContext)
        for(index in 0..2){
            faintedAndEndBattleArray[index] = incomingArray[index]
        }

            //If the battle shouldn't end
            if(!faintedAndEndBattleArray[0]){
                //Force Switch enemy team if their active pokemon faints
                if(faintedAndEndBattleArray[1] && !faintedAndEndBattleArray[2]){
                    forceEnemySwitch = true
                }

                //Force Switch player team if their active pokemon faints
                if(faintedAndEndBattleArray[1] && faintedAndEndBattleArray[2]){
                    forcePlayerSwitch[0] = true
                }
            } else {
                //Flip end battle boolean
                endBattle = true
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
            //passing the hp to max hp

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
        binding.pokemon1Img.setImageResource(getPokemonImageResourceId(playerActivePokemon.pokemon.battleStats.species))

        binding.pokemon2Name.text = enemyActivePokemon.pokemon.name
        val ePokemonLevel = "level: " + enemyActivePokemon.pokemon.level
        binding.pokemon2Level.text = ePokemonLevel
        val ePokemon2Hp = "Hp: " + enemyActivePokemon.pokemon.hp + "/" + enemyActivePokemon.pokemon.maxHp
        binding.pokemon2Hp.text = ePokemon2Hp
        binding.pokemon2Img.setImageResource(getPokemonImageResourceId(enemyActivePokemon.pokemon.battleStats.species))
    }

    fun showBattleText(text: String){
        val buttons = binding.buttons
        val battleText = binding.battleText
        battleText.visibility = VISIBLE
        battleText.text = text
        buttons.visibility = INVISIBLE
    }

    fun addStringToBattleTextList(text: String, updatesHP: Boolean? = null,
                             doesHeal: Boolean? = null, isOpponent: Boolean? = null){
        battleTextList.add(BattleText(text, updatesHP, doesHeal, isOpponent))
    }

    fun addBattleTextToBattleTextList(battleText: BattleText){
        battleTextList.add(battleText)
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        //positive button
        TrainerBattleLog.info("Trainer chose yes")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        // User touched the dialog's negative button
        TrainerBattleLog.info("Trainer chose no")
    }

    //Upon action_down, progress the battle
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //If we press the screen...
        if(event!!.action == MotionEvent.ACTION_DOWN) {
            //If the log trail isn't empty, show it and hide the buttons. If it isn't empty, show the buttons
            if (battleTextList.isNotEmpty()) {
                val text = battleTextList.removeFirst()
                showBattleText(text.message)
                //If a battle move was performed, update the HP of the involved pokemon early
                if(text.updatesHP == true) {
                    if(text.doesHeal == true) {
                        if (text.isOpponent == true) {
                            val newHpEnemy =
                                "Hp: " + enemyActivePokemon.pokemon.hp + "/" + enemyActivePokemon.pokemon.maxHp
                            binding.pokemon2Hp.text = newHpEnemy
                        } else {
                            val newHpPlayer =
                                "Hp: " + playerActivePokemon.pokemon.hp + "/" + playerActivePokemon.pokemon.maxHp
                            binding.pokemon1Hp.text = newHpPlayer
                        }
                    } else {
                        if (text.isOpponent == true) {
                            val newHpPlayer =
                                "Hp: " + playerActivePokemon.pokemon.hp + "/" + playerActivePokemon.pokemon.maxHp
                            binding.pokemon1Hp.text = newHpPlayer
                        } else {
                            val newHpEnemy =
                                "Hp: " + enemyActivePokemon.pokemon.hp + "/" + enemyActivePokemon.pokemon.maxHp
                            binding.pokemon2Hp.text = newHpEnemy
                        }
                    }
                }
            } else {
                binding.battleText.visibility = INVISIBLE
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

                if(faintedAndEndBattleArray[1]){
                    if(faintedAndEndBattleArray[2]) {
                        //If fainted pokemon is in player's team, reward enemy pokemon by not calling the fragment manager
                        Level().addExperience(enemyActivePokemon.pokemon, gainedExperience[0], this)
                    } else {
                        //If fainted pokemon is not in player's team, reward player pokemon by calling fragment manager
                        Level(fragmentManager).addExperience(playerActivePokemon.pokemon, gainedExperience[0], this)
                    }
                }

                if(endBattle){
                    //Return to main menu
                    returnToMenu()
                }

                if(forceEnemySwitch){
                    forceEnemySwitch = false
                    for(pokemon in enemyTeam){
                        if(pokemon.hp > 0){
                            showBattleText("Opponent's ${enemyActivePokemon.pokemon.name} was withdrawn!")
                            enemyActivePokemon = ActivePokemon(enemyTeam[enemyTeam.indexOf(pokemon)], null, enemyTeam.indexOf(pokemon), false)
                            BattlePhase.BattleLog.info("Opponent's ${enemyActivePokemon.pokemon.name} switched in!")
                            showBattleText("Opponent's ${enemyActivePokemon.pokemon.name} switched in!")
                            break
                        }
                    }
                }
                if(forcePlayerSwitch[0]){
                    switch(playerTeam, teamPositionArray, fragmentManager)
                }
                updateUI(playerActivePokemon,enemyActivePokemon)
            }
        }
        return super.onTouchEvent(event)
    }

}