package com.example.pokemongame.pokemon

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@Dao
interface PokemonDao {
    @Query("SELECT * FROM Pokemon WHERE inTeam = 1 ORDER BY ordering ASC LIMIT 6")
    fun getTeam(): List<Pokemon>

    @Query("SELECT * FROM Pokemon WHERE inTeam = 0 ORDER BY ordering ASC")
    fun getCollection(): List<Pokemon>

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

object Converters {
    @TypeConverter
    fun fromString(value: String?): List<String> {
        val listType: Type = object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromList(list: List<String?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
    @TypeConverter
    fun fromArrayList(list: ArrayList<Move?>?): String{
        val gson = Gson()
        return gson.toJson(list)
    }
    @TypeConverter
    fun fromMoveString(value: String?): ArrayList<Move> {
        val type: Type = object : TypeToken<ArrayList<Move?>?>() {}.type
        return Gson().fromJson(value, type)
    }
}

@Database(entities = [Pokemon ::class, BattleStats ::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun PokemonDao(): PokemonDao
    abstract fun BattleStatsDao(): BattleStatsDao
}

public fun saveToDB(team: ArrayList<Pokemon>, collection: ArrayList<Pokemon>, db:AppDatabase){
    for(i in 0 until team.size){
        team[i].ordering = i
        team[i].inTeam = true
    }
    for(i in 0 until collection.size){
        collection[i].ordering = i
        collection[i].inTeam = false
    }
    db.PokemonDao().insert(team + collection)
}

