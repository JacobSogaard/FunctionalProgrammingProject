package com.example.tournamentandroid.Presenters

import enums.TournamentType
import models.Game
import models.Team
import models.Tournament

class TournamentPresenter(type: TournamentType, var teams: ArrayList<Team>) {

    private var tournament = Tournament(type, teams)

    fun getAllTeams(): List<Team> {
        return tournament.getAllTeams()
    }

    fun setRandom(bool: Boolean){
        tournament.setRandom(bool)
    }

    fun getCupLimit(): Int{
        return tournament.getCupLimit()
    }

    fun getConcludedMatches(): List<Game?>? {
        return tournament.getConcludedMatches()
    }

    fun start(){
        tournament.start()
    }

    fun getCurrentRound(): Int {
        return tournament.getCurrentRound()
    }

    fun getMaxRounds(): Int {
        return tournament.getMaxRounds()
    }

    fun hasRound(round: Int): Boolean {
        return getMaxRounds() >= round
    }

    fun getMatchesForRound(round: Int): List<Game> {
        return tournament.getMathcesForRound(round)
    }


    fun concludeMatch(match: Game) {
        tournament.concludeMatch(match)
    }

    fun isDone(): Boolean? {
        return tournament.isDone()
    }


    fun getNextMatch(): Game? {
        return tournament.getNextMatch()
    }

    fun setWinner(team: Team){
        tournament.setWinner(team)
    }

}