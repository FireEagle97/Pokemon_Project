package com.example.pokemongame.pokemon

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
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
        }

        return simplified

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
                "base_exp_reward",
                json["base_experience"].asInt
            )
            json["stats"].asJsonArray.associate {
                it.asJsonObject.run {
                    (this["stat"].asJsonObject["name"].asString) to (this["base_stat"].asInt)
                }
            }.forEach {
                val statName = when (it.key) {
                    "hp" -> "maxHp"
                    "special-defense" -> "special_defense"
                    "special-attack" -> "special_attack"
                    else -> it.key
                }

                this.addProperty("base_$statName", it.value)
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

        return gsonObj.fromJson(simplified, battleStatsType)
    }

}
