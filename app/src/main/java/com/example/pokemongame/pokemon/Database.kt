package com.example.pokemongame.pokemon

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {
    @Query("SELECT * FROM Pokemon WHERE inTeam = 1 ORDER BY ordering ASC LIMIT 6")
    fun getTeam()

    @Query("SELECT * FROM Pokemon WHERE inTeam = 0 ORDER BY ordering ASC")
    fun getCollection()

    @Insert(onConflict = OnConflictStrategy.REPLACE) // since some attributes will be changed.
    fun insert(list: List<Pokemon>)
}

@Dao
interface BattleStatsDao {
    @Query("SELECT * FROM BattleStats WHERE species = :sp LIMIT 1")
    fun getBattleStats(sp: String): BattleStats

    @Query("SELECT EXISTS(SELECT * FROM BattleStats WHERE species = :sp)")
    fun checkIfExists(sp: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(battleStats: BattleStats)
}