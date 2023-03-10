package com.example.pokemongame.pokemon

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
class PokeAPI {

    private val gsonObj: Gson = GsonBuilder().setPrettyPrinting().create()


    fun getPokemonSprite(apiResponse: String?): JsonObject {
        val json = gsonObj.fromJson(apiResponse, JsonObject::class.java)

        val simplified = JsonObject().apply {
            addProperty(
                "front_sprite",
                json["sprites"].asJsonObject["versions"].asJsonObject["generation-i"]
                    .asJsonObject["red-blue"].asJsonObject["front_transparent"].asString
            )
            addProperty(
                "back_sprite",
                json["sprites"].asJsonObject["versions"].asJsonObject["generation-i"]
                    .asJsonObject["red-blue"].asJsonObject["back_transparent"].asString
            )
        }

        return simplified

    }

    fun simplifyPokedexEntries(apiResponse: String): List<Pokedex> {
        val json = gsonObj.fromJson(apiResponse, JsonObject::class.java)
        val simplified = json["pokemon_entries"].asJsonArray.map {
            JsonObject().apply {
                addProperty(
                    "number",
                    it.asJsonObject["entry_number"].asInt
                )
                addProperty(
                    "name",
                    it.asJsonObject["pokemon_species"].asJsonObject["name"].asString
                )
            }
        }
        val simplifiedStr = gsonObj.toJson(simplified)
        val pokedexType =  object : TypeToken<List<Pokedex>>() {}.type
        val pokedexEntries : List<Pokedex> = gsonObj.fromJson(simplifiedStr,pokedexType)

        return pokedexEntries
    }


    // Function to convert string to URL
    private fun stringToURLConverter(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }

    // Function to establish connection and load image
    fun imageLoader(string: String): Bitmap? {
        val url: URL = stringToURLConverter(string)!!
        var connection: HttpURLConnection?
        try{
            connection = url.openConnection() as HttpURLConnection
            connection!!.connect()
            val inputStream: InputStream = connection!!.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            val bitmap = BitmapFactory.decodeStream(bufferedInputStream)
            return bitmap
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun simplifyBattleStats(apiResponse: String?): BattleStats {
        val json = gsonObj.fromJson(apiResponse, JsonObject::class.java)

        val simplified = JsonObject().apply {
            addProperty(
                "species",
                json["name"].asString
            )
            addProperty(
                "baseExperienceReward",
                json["base_experience"].asInt
            )
            json["stats"].asJsonArray.associate {
                it.asJsonObject.run {
                    (this["stat"].asJsonObject["name"].asString) to (this["base_stat"].asInt)
                }
            }.forEach {
                val statName = when (it.key) {
                    "hp" -> "MaxHp"
                    "attack" -> "Attack"
                    "defense" -> "Defense"
                    "special-defense" -> "SpecialDefense"
                    "special-attack" -> "SpecialAttack"
                    "speed" -> "Speed"
                    else -> it.key
                }

                this.addProperty("baseStat$statName", it.value)
            }
            add(
                "types",
                JsonArray().apply {
                    json["types"].asJsonArray.map {
                        it.asJsonObject["type"].asJsonObject["name"].asString
                    }.forEach { this.add(it) }
                }
            )

        }
        val battleStatsType = object : TypeToken<BattleStats>() {}.type
        val battleStats = gsonObj.fromJson<BattleStats>(simplified, battleStatsType)
        return gsonObj.fromJson(simplified, battleStatsType)
    }

}
