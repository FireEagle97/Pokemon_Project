package com.example.pokemongame.battle

data class BattleText(var message: String, var updatesHP: Boolean? = null,
                      var doesHeal: Boolean?, var isOpponent: Boolean? = null)