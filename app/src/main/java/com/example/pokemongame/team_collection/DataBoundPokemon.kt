package com.example.pokemongame.team_collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class DataBoundPokemon : ViewModel() {
    private val mutableTeam = MutableLiveData<String>()
    private val mutableCollection = MutableLiveData<String>()

    val team: LiveData<String> get() = mutableTeam
    val collection: LiveData<String> get() = mutableCollection
    fun setTeam(item: String) {
        mutableTeam.value = item
    }
    fun setCollection(item: String){
        mutableCollection.value = item
    }
    fun getTeamSize(): Int{
        return team.
    }
}
