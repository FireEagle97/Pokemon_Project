package com.example.pokemongame.pokemon

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
class PokeAPI {


    private val LOG_TAG = "POKEAPI"

    val POKE_API_BASE_URL = "https://pokeapi.co/api/v2"
    val POKEDEX_BASE_URL = "$POKE_API_BASE_URL/pokedex/2"
    val POKEMON_BASE_URL = "$POKE_API_BASE_URL/pokemon"
    private val GSON: Gson = GsonBuilder().setPrettyPrinting().create()

    /**
     * Simplifies the API response from the pokedex endpoint.
     * The simplified JSON has the following format :
     * ```
     * [
     *   {
     *     "number" : 1,
     *     "name" : "bulbasaur"
     *   },
     *   {
     *     "number" : 2,
     *     "name" : "ivysaur"
     *   },
     *   ...
     *   {
     *     "number" : 151,
     *     "name" : "mew"
     *   }
     * ]
     * ```
     *
     * @param apiResponse the API response as received from PokeAPI's pokedex endpoint
     *                    (https://pokeapi.co/api/v2/pokedex)
     *
     * @return the simplified JSON as a String
     */
    fun simplifyPokedexEntries(apiResponse: String?): List<Pokedex> {
        val json = GSON.fromJson(apiResponse, JsonObject::class.java)

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
        val simplifiedStr = GSON.toJson(simplified)
        val pokedexType =  object : TypeToken<List<Pokedex>>() {}.type
        val pokedexEntries : List<Pokedex> = GSON.fromJson(simplifiedStr,pokedexType)

        return pokedexEntries
    }

    /**
     * Simplifies the API response from the pokemon endpoint.
     * The simplified JSON has the following format :
     * ```
     * {
     *   "name" : "bulbasaur",
     *   "base_exp_reward" : 64,
     *   "types" : [
     *     "grass", "poison"
     *   ],
     *   "base_maxHp" : 45,
     *   "base_attack" : 49,
     *   "base_defense" : 49,
     *   "base_special-attack" : 65,
     *   "base_special-defense" : 65,
     *   "base_speed" : 45,
     *   "back_sprite" : "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-i/red-blue/transparent/back/1.png"
     *   "front_sprite" : "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-i/red-blue/transparent/1.png"
     * }
     * ```
     *
     * @param apiResponse the API response as received from PokeAPI's pokemon endpoint
     *                    (https://pokeapi.co/api/v2/pokemon)
     *
     * @return the simplified JSON as a String
     */
    fun simplifyPokemon(apiResponse: String?): Pokemon {
        val json = GSON.fromJson(apiResponse, JsonObject::class.java)

        val simplified = JsonObject().apply {
            addProperty(
                "name",
                json["name"].asString
            )
            addProperty(
                "base_exp_reward",
                json["base_experience"].asInt
            )
            add(
                "types",
                JsonArray().apply {
                    json["types"].asJsonArray.map {
                        it.asJsonObject["type"].asJsonObject["name"].asString
                    }.forEach { this.add(it) }
                }
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
            addProperty(
                "back_sprite",
                json["sprites"].asJsonObject["versions"].asJsonObject["generation-i"]
                    .asJsonObject["red-blue"].asJsonObject["back_transparent"].asString
            )
            addProperty(
                "front_sprite",
                json["sprites"].asJsonObject["versions"].asJsonObject["generation-i"]
                    .asJsonObject["red-blue"].asJsonObject["front_transparent"].asString
            )
        }
        val pokemonType = object : TypeToken<Pokemon>() {}.type

        return GSON.fromJson(simplified, pokemonType)
    }
    @Throws(IOException::class, UnsupportedEncodingException::class)
    fun readApiResponse(stream: InputStream?): String {
        var bytesRead: Int
        var totalRead = 0
        val buffer = ByteArray(NETIOBUFFER)
        val reader = BufferedReader(stream?.reader())
        var content: String
        try {
            content = reader.readText()
        } finally {
            reader.close()
        }


        return content
    }
    companion object {
        private val NETIOBUFFER = 1024

        private val TAG = "HttpURLConn"
    }

    @Throws(IOException::class)
    fun getApiRes(myurl: String): String? {
        Log.d(TAG, "downloadUrl $myurl")

        var istream: InputStream? = null
        // Only read the first 500 characters of the retrieved
        // web page content.
        // int len = MAXBYTES;
        var conn: HttpURLConnection? = null
        val url = URL(myurl)
        try {
            // create and open the connection
            conn = url.openConnection() as HttpURLConnection

            /*
			 * set maximum time to wait for stream read read fails with
			 * SocketTimeoutException if elapses before connection established
			 *
			 * in milliseconds
			 *
			 * default: 0 - timeout disabled
			 */
            conn.readTimeout = 10000
            /*
			 * set maximum time to wait while connecting connect fails with
			 * SocketTimeoutException if elapses before connection established
			 *
			 * in milliseconds
			 *
			 * default: 0 - forces blocking connect timeout still occurs but
			 * VERY LONG wait ~several minutes
			 */
            conn.connectTimeout = 15000
            /*
			 * HTTP Request method defined by protocol
			 * GET/POST/HEAD/POST/PUT/DELETE/TRACE/CONNECT
			 *
			 * default: GET
			 */
            conn.requestMethod = "GET"
            // specifies whether this connection allows receiving data
            conn.doInput = true
            // Starts the query
            conn.connect()

            val response = conn.responseCode
            Log.d(TAG, "Server returned: $response")

            /*
			 *  check the status code HTTP_OK = 200 anything else we didn't get what
			 *  we want in the data.
			 */
            if (response != HttpURLConnection.HTTP_OK)
                return "Server returned: $response aborting read."

            // get the stream for the data from the website
            istream = conn.inputStream
            val a = BufferedInputStream(istream)
            // read the stream, returns String
            return readApiResponse(istream)
        } catch (e: IOException) {
            Log.e(TAG, "IO exception in bg")
            Log.getStackTraceString(e)
            throw e
        } finally {
            /*
			 * Make sure that the InputStream is closed after the app is
			 * finished using it.
			 * Make sure the connection is closed after the app is finished using it.
			 */

            if (istream != null) {
                try {
                    istream.close()
                } catch (ignore: IOException) {
                }

                if (conn != null)
                    try {
                        conn.disconnect()
                    } catch (ignore: IllegalStateException) {
                    }

            }
        }
    }





}
