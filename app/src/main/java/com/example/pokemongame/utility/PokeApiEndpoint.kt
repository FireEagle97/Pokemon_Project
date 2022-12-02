package com.example.pokemongame.utility

enum class PokeApiEndpoint(val url: String) {
    BASE("https://pokeapi.co/api/v2"),

    /**
     * Pokemon and their moves have types (fire, water, dragon, grass, etc.). For example, the
     * Pokemon Bulbasaur has 2 types : grass and poison. Independently, the move "bubble" is a water
     * type move.
     *
     * ``
     *
     * Different Pokemon games have a different list of types that exist. For example, later games
     * added the type "fairy", a type that is not in the original "Pokemon Red" (the game we base
     * our project on).
     *
     * ``
     *
     * This endpoint responds with a lot of data (including the list of existing types), given the
     * generation name.
     *
     * We'll always use the first generation :
     * ```
     * "${com.example.pokemongame.utility.PokeApiEndpoint.GENERATION.url}/generation-i"
     * ```
     * Or you can specify it, with the number 0 instead :
     * ```
     * "${com.example.pokemongame.utility.PokeApiEndpoint.GENERATION.url}/0"
     * ```
     *
     * The response from this endpoint should be passed to [simplifyTypes]
     */
    GENERATION("${BASE.url}/generation"),

    /**
     * Moves of a given type are more or less effective against opponent Pokemon of a certain type.
     * For example, a fire type move will be more effective (i.e. do more damage) against a Pokemon
     * with the grass type while the same fire type move will be less effective (i.e. do less
     * damage) against a Pokemon with the water type.
     *
     * This endpoint responds with a lot of data related to a given type (including which types it's
     * more/less effective against) :
     * ```
     * "${com.example.pokemongame.utility.PokeApiEndpoint.TYPE.url}/<type name>"
     * ```
     *
     * The response from this endpoint should be passed to [simplifyTypeRelations]
     */
    TYPE("${BASE.url}/type"),

    /**
     * The Pokedex is the list of Pokemon names and numbers (each uniquely representing a Pokemon).
     *
     * ``
     *
     * Different Pokemon games have different Pokedexes (i.e. they each have a different list of
     * Pokemon). For example, "Pokemon Red" (the game we base our project on) has 151 Pokemon, while
     * "Pokemon Ruby" (a newer version) has 201.
     *
     * ``
     *
     * This endpoint responds with a lot of data (including the Pokedex), given the Pokedex name.
     *
     * We'll always use the kanto Pokedex :
     * ```
     * "${com.example.pokemongame.utility.PokeApiEndpoint.POKEDEX.url}/kanto"
     * ```
     * Or you can specify it, with the number 2 instead :
     * ```
     * "${com.example.pokemongame.utility.PokeApiEndpoint.POKEDEX.url}/2"
     * ```
     *
     * The response from this endpoint should be passed to [simplifyPokedexEntries]
     */
    POKEDEX("${BASE.url}/pokedex"),

    /**
     * Each individual Pokemon has data related to it (its species, type, stats, sprites, moves it
     * can learn, etc.)
     *
     * ``
     *
     * The endpoint responds with the Pokemon data (including its moves), given the Pokemon name :
     * ```
     * "${com.example.pokemongame.utility.PokeApiEndpoint.POKEMON.url}/<pokemon name>"
     * ```
     *
     * The response from this endpoint should be passed to [simplifyPokemon] or [simplifyMoves]
     */
    POKEMON("${BASE.url}/pokemon"),

    /**
     * Each individual move has data related to it (its name, type, accuracy, power, etc.)
     *
     * ``
     *
     * The endpoint responds with the data for the move, given the name of that move :
     * ```
     * "${com.example.pokemongame.utility.PokeApiEndpoint.MOVE.url}/<move name>"
     * ```
     *
     * The response from this endpoint should be passed to [simplifyMove]
     */
    MOVE("${BASE.url}/move")
}