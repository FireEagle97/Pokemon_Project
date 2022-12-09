package com.example.pokemongame.pokemon

import android.content.Context
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

@Entity
data class TrainerName(@PrimaryKey val name: String)

@Dao
interface TrainerNameDao {
    @Query("SELECT * FROM TrainerName LIMIT 1")
    fun getTrainerName():String

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(trainerName: TrainerName)
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

@Database(entities = [Pokemon ::class, BattleStats ::class, TrainerName ::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun PokemonDao(): PokemonDao
    abstract fun BattleStatsDao(): BattleStatsDao
    abstract fun TrainerNameDao(): TrainerNameDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE IS NOT NULL , then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "PokemonDatabase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                //return instance
                instance
            }

        }
    }

}


fun saveToDB(team: ArrayList<Pokemon>, collection: ArrayList<Pokemon>, db:AppDatabase){
    for(i in 0 until team.size){
        team[i].ordering = i
        team[i].inTeam = true
        if(!db.BattleStatsDao().checkIfExists(team[i].species)){
            db.BattleStatsDao().insert(team[i].battleStats)
        }
    }
    for(i in 0 until collection.size){
        collection[i].ordering = i
        collection[i].inTeam = false
        if(!db.BattleStatsDao().checkIfExists(collection[i].species)){
            db.BattleStatsDao().insert(collection[i].battleStats)
        }
    }
    db.PokemonDao().insert(team + collection)
}

/**
 * returns an arraylist, index 0 will be the team index 1 will be the collection.
 */
fun getTeamAndCol(db:AppDatabase): ArrayList<List<Pokemon>>{
    val teamAndCollection = ArrayList<List<Pokemon>>()
    teamAndCollection.add(db.PokemonDao().getTeam())
    teamAndCollection.add(db.PokemonDao().getCollection())
    for(i in 0 until teamAndCollection.size){
        for(k in 0 until teamAndCollection[i].size){
            teamAndCollection[i][k].battleStats = db.BattleStatsDao().getBattleStats(teamAndCollection[i][k].species)

        }
    }
    return teamAndCollection
}

