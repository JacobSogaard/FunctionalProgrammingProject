package com.example.tournamentandroid.models;

import models.Game;
import models.Team;

interface ITournament {

    fun getAllTeams(): List<Team>

    fun setRandom(bool: Boolean)

    fun setCupLimit(limit: Int)

    fun getCupLimit(): Int

    fun getConcludedMatches(): List<Game?>?

    fun start()

    fun getCurrentRound(): Int

    fun getMaxRounds(): Int

    fun hasRound(round: Int): Boolean

    fun getMathcesForRound(round: Int): List<Game>

    fun isDone(): Boolean?

    fun getNextMatch(): Game?

    fun concludeMatch(match: Game)

    fun setWinner(team:Team?)

    fun get(): Int

}
