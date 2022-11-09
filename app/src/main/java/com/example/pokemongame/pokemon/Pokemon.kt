package com.example.pokemongame.pokemon

import android.content.res.Resources
import org.json.JSONObject;
import org.json.JSONException;
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.text.Charsets.UTF_8

class Pokemon(level : String, species : String, name : String = species) {
    private var experienceReward: Int = 0;
    private var attack : Int = 0;
    private var defense : Int = 0;
    private var maxHp : Int = 0;
    private var specialAttack : Int = 0;
    private var specialDefence : Int = 0;
    private lateinit var types : Array<String>;

    //fun parse json
    fun getAttributes(){
        try{
            val obj = JSONObject(getJSONFromAssets()!!);
        }catch(e: JSONException){
            e.printStackTrace()
        }

//        var gson = Gson();
//        Resources.
//        val a = gson.fromJson(Resources., JsonObject::class.java)
//        a.asInt("")
//        a.asInt
//        var jsonString = gson.fromJson<>()

    }

    /**
     * Method to Load the JSON from the Assets file and return the object
     */
    private fun getJSONFromAssets(): String? {
        var json: String? = null
        val charset: Charset = Charsets.UTF_8
        try{
            val `is` = assets.open("bulbasaur.json")

        }
    }
    //assign each field to its coressponding value in the json file

}