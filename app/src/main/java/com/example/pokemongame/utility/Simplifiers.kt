import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject

private val GSON: Gson = GsonBuilder().setPrettyPrinting().create()

/**
 * Helper function for all of the "simplify*" functions.
 *
 * ``
 *
 * Converts the string api response to a JsonObject for the simplifier function to use and then
 * convert its result back into a JSON string
 *
 * @param apiResponse the raw string api response (it's a JSON string)
 * @param simplifier the "simplify*" function
 *
 * @return the simplified response as a JSON string
 */
private fun simplifyApiResponse(
    apiResponse: String,
    simplifier: (json: JsonObject) -> Any
): String =
    GSON.toJson(simplifier(GSON.fromJson(apiResponse, JsonObject::class.java)))

/**
 * Simplifies the response from the [PokeApiEndpoint.GENERATION] endpoint.
 *
 * ``
 *
 * The simplified response has the following format :
 * ```
 * [
 *  "normal",
 *  "fire",
 *  ...
 *  "dragon"
 * ]
 * ```
 *
 * @param apiResponse The JSON string response from the [PokeApiEndpoint.GENERATION] endpoint.
 *
 * @return the simplified response as a JSON string
 */
fun simplifyTypes(apiResponse: String): String =
    simplifyApiResponse(apiResponse) { json ->
        json["types"].asJsonArray.map { it.asJsonObject["name"].asString }
    }

/**
 * Simplifies the response from [PokeApiEndpoint.TYPE] endpoint.
 *
 * ``
 *
 * The simplified response has the following format :
 * ```
 * {
 *  "fire" : "super_effective",
 *  "electric" : "super_effective",
 *  "grass" : "not_very_effective",
 *  "poison" : "super_effective",
 *  "flying" : "no_effect",
 *  "bug" : "not_very_effective",
 *  "rock" : "super_effective",
 *  "steel" : "super_effective"
 * }
 * ```
 *
 * For example, the format given above is the response you would get when requesting
 * ```
 * "${com.example.pokemongame.utility.PokeApiEndpoint.TYPE.url}/ground"
 * ```
 * What this means is that a ground type attack is super effective against a fire type opponent
 * Pokemon, not very effective against a grass type opponent Pokemon, has no effect against a flying
 * type opponent Pokemon, etc.
 *
 * When a type isn't listed, then it's implied that an opponent Pokemon of this omitted type will
 * be normally affected by the attack (e.g. ice is omitted, thus a ground type attack normally
 * affects an ice type opponent Pokemon).
 *
 * When an invalid type is listed (e.g. steel), you can simply ignore it. This is a consequence of
 * how the PokeAPI is designed.
 *
 * ``
 *
 * Included in this KDoc is a chart representing the type relations you should expect to see (but
 * shouldn't hardcode in your application).
 * @see <a href="https://img.pokemondb.net/images/typechart-gen1.png">Type Chart</a>
 *
 * @param apiResponse The JSON string response from the [PokeApiEndpoint.TYPE] endpoint.
 *
 * @return the simplified response as a JSON string
 */
fun simplifyTypeRelations(apiResponse: String): String =
    simplifyApiResponse(apiResponse) { json ->
        val firstGeneration = "generation-i"

        val damageRelations =
            if (json["generation"].asJsonObject["name"].asString == firstGeneration) {
                json["damage_relations"].asJsonObject
            } else {
                json["past_damage_relations"].asJsonArray.first {
                    it.asJsonObject["generation"].asJsonObject["name"].asString == firstGeneration
                }.asJsonObject
            }

        val map = mutableMapOf<String, String>()

        damageRelations["double_damage_to"].asJsonArray.map { it.asJsonObject["name"].asString }
            .forEach { typeName -> map[typeName] = "super_effective" }
        damageRelations["half_damage_to"].asJsonArray.map { it.asJsonObject["name"].asString }
            .forEach { typeName -> map[typeName] = "not_very_effective" }
        damageRelations["no_damage_to"].asJsonArray.map { it.asJsonObject["name"].asString }
            .forEach { typeName -> map[typeName] = "no_effect" }

        map
    }

/**
 * Simplifies the response from [PokeApiEndpoint.POKEDEX] endpoint.
 *
 * ``
 *
 * The simplified response has the following format :
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
 * You are not required to use the "number" field for anything in the project, but it's there if you
 * want to do something with it.
 *
 * @param apiResponse The JSON string response from the [PokeApiEndpoint.POKEDEX] endpoint.
 *
 * @return the simplified response as a JSON string
 */
fun simplifyPokedexEntries(apiResponse: String): String =
    simplifyApiResponse(apiResponse) { json ->
        json["pokemon_entries"].asJsonArray.map {
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
    }

/**
 * Simplifies the response from [PokeApiEndpoint.POKEMON] endpoint (to get the Pokemon data).
 *
 * ``
 *
 * The simplified response has the following format :
 * ```
 * {
 *   "species" : "bulbasaur",
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
 * @param apiResponse The JSON string response from the [PokeApiEndpoint.POKEMON] endpoint.
 *
 * @return the simplified response as a JSON string
 */
fun simplifyPokemon(apiResponse: String): String =
    simplifyApiResponse(apiResponse) { json ->
        JsonObject().apply {
            addProperty(
                "species",
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
                val statName = if (it.key == "hp") "maxHp" else it.key
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
    }

/**
 * You may ignore this constant, it's used by [simplifyMoves] and [getMoveDescription]
 */
private val VERSIONS = listOf("red-blue", "yellow")

/**
 * Simplifies the response from [PokeApiEndpoint.POKEMON] endpoint (to get the list of moves learned
 * by a Pokemon).
 *
 * ``
 *
 * The simplified response has the following format :
 * ```
 * [
 *   {
 *     "move" : "vine-whip",
 *     "level" : 13
 *   },
 *   {
 *     "move" : "tackle",
 *     "level" : 1
 *   },
 *   ...
 *   {
 *     "move" : "solar-beam",
 *     "level" : 48
 *   }
 * ]
 * ```
 *
 * @param apiResponse The JSON string response from the [PokeApiEndpoint.POKEMON] endpoint.
 *
 * @return the simplified response as a JSON string
 */
fun simplifyMoves(apiResponse: String): String =
    simplifyApiResponse(apiResponse) { json ->
        json["moves"].asJsonArray.mapNotNull { move ->
            move.asJsonObject["version_group_details"].asJsonArray
                /*
                 * Keep the move, only if it's in a version that matches a version in the given
                 * version parameter (i.e. it's a move that's available in the versions supported
                 * by the application) and if it's a move learned by leveling-up.
                 */
                .mapNotNull versionMapping@{ versionGroupDetail ->
                    val levelLearned = versionGroupDetail.asJsonObject["level_learned_at"].asInt
                    val learnMethod =
                        versionGroupDetail.asJsonObject["move_learn_method"].asJsonObject["name"].asString
                    val versionName =
                        versionGroupDetail.asJsonObject["version_group"].asJsonObject["name"].asString

                    return@versionMapping if (
                        learnMethod == "level-up" &&
                        VERSIONS.contains(versionName)
                    ) {
                        (versionName to levelLearned)
                    } else {
                        null
                    }
                }
                /*
                 * The versions parameter is ordered by preference of version, we keep the move's
                 * version from the preferred game version (i.e. we'll keep the "red-blue" version
                 * of the move, rather than the "yellow" version of the move)
                 */
                .minByOrNull { versionLevelPair ->
                    VERSIONS.indexOf(versionLevelPair.first)
                }?.let { versionLevelPair ->
                    mapOf(
                        "move" to move.asJsonObject["move"].asJsonObject["name"].asString,
                        "level" to versionLevelPair.second
                    )
                }
        }
    }

/**
 * Gets a potential move description from the given move data.
 *
 * ``
 *
 * Helper function for the [simplifyMove] function.
 *
 * @param json The response from the [PokeApiEndpoint.MOVE] endpoint (parsed as a JsonObject).
 *
 * @return a move description (might be null)
 */
private fun getMoveDescription(json: JsonObject): String? =
    json["flavor_text_entries"].asJsonArray.firstOrNull {
        /*
         * This seems to be the earliest version group with a flavor text. We could use the
         * "effect_entry" property instead, but it contains things like :
         * "Has a $effect_chance% chance to burn the target."
         */
        val version = "gold-silver"

        /* We want a description in english, and for the right version */
        it.asJsonObject["language"].asJsonObject["name"].asString == "en" &&
                it.asJsonObject["version_group"].asJsonObject["name"].asString == version
    }?.let {
        it.asJsonObject["flavor_text"].asString.replace('\n', ' ')
    }

/**
 * Gets the move target from the given move data.
 *
 * ``
 *
 * Helper function for the [simplifyMove] function.
 *
 * @param json The response from the [PokeApiEndpoint.MOVE] endpoint (parsed as a JsonObject).
 *
 * @return the move target
 */
private fun getMoveTarget(json: JsonObject): String =
    when (json["target"].asJsonObject["name"].asString) {
        in listOf(
            "selected-pokemon-me-first",
            "random-opponent",
            "all-other-pokemon",
            "selected-pokemon",
            "all-opponents"
        ) -> "opponent"
        in listOf("user-or-ally", "user", "user-and-allies", "all-allies") -> "self"
        "all-pokemon" -> "both"
        else -> "other"
    }

/**
 * You may ignore this constant, it's used by [simplifyMove]
 */
private val SUPPORTED_MOVE_CATEGORIES = listOf(
    "damage",
    "ailment",
    "heal",
    "damage+ailment"
)

/**
 * You may ignore this constant, it's used by [simplifyMove]
 */
private val SUPPORTED_MOVE_AILMENTS = listOf(
    "paralysis",
    "sleep",
    "freeze",
    "burn",
    "poison",
    "confusion"
)

/**
 * Simplifies the response from [PokeApiEndpoint.MOVE] endpoint.
 *
 * ``
 *
 * The simplified response has the following format :
 * ```
 * {
 *   "name" : "ember",
 *   "description" : "An attack that may inflict a burn.",
 *   "category" : "damage+ailment",
 *   "accuracy" : 100,
 *   "power" : 40,
 *   "damage_class" : "special"
 *   "type" : "fire",
 *   "maxPP" : 25,
 *   "ailment" : "burn",
 *   "ailment_chance" : 10,
 *   "healing" : 0
 * }
 * ```
 * 
 * You are not required to use the "description" field for anything in the
 * project, but it's there if you want to do something with it.
 *
 * A few important notes on the simplified response format :
 * - "description" might be a null value
 * - "category" is one of "damage", "ailment", "heal", "damage+ailment" or "other"
 * - "accuracy", "ailment_chance", "healing" are all between 0 and 100 (both inclusive)
 * - "damage_class" is either "physical" or "special"
 * - "ailment" is one of "paralysis", "sleep", "freeze", "burn", "poison", "confusion" or a null value
 *
 * @param apiResponse The JSON string response from the [PokeApiEndpoint.POKEMON] endpoint.
 *
 * @return the simplified response as a JSON string
 */
fun simplifyMove(apiResponse: String): String =
    simplifyApiResponse(apiResponse) { json ->
        /* A portion of the API response containing a lot of metadata */
        val meta = json["meta"].asJsonObject

        JsonObject().apply {
            addProperty(
                "name",
                json["name"].asString
            )
            addProperty(
                "description",
                getMoveDescription(json)
            )
            addProperty(
                "category",
                meta["category"].asJsonObject["name"].asString.let {
                    if (it in SUPPORTED_MOVE_CATEGORIES) it else "other"
                }
            )
            addProperty(
                "accuracy",
                json["accuracy"].run { if (isJsonNull) 100 else asInt }
            )
            addProperty(
                "power",
                json["power"].run { if (isJsonNull) 0 else asInt }
            )
            addProperty(
                "damage_class",
                json["damage_class"].asJsonObject["name"].asString.let {
                    if (it == "physical") "physical" else "special"
                }
            )
            addProperty(
                "type",
                json["type"].asJsonObject["name"].asString
            )
            addProperty(
                "maxPP",
                json["pp"].asInt
            )
            addProperty(
                "target",
                getMoveTarget(json)
            )
            addProperty(
                "ailment",
                meta["ailment"].asJsonObject["name"].asString.let {
                    if (it in SUPPORTED_MOVE_AILMENTS) it else null
                }
            )
            addProperty(
                "ailment_chance",
                meta["ailment_chance"].asInt
            )
            addProperty(
                "healing",
                meta["healing"].run { if (isJsonNull) 0 else asInt }
            )
        }
    }
