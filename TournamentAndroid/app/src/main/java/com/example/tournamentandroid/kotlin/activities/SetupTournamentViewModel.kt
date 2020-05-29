package org.jetbrains.kotlin.activities

import Presenters.SetupTournamentPresenter
import androidx.lifecycle.ViewModel
import enums.TournamentType
import models.Team


class SetupTournamentViewModel : ViewModel() {
    private val _presenter: SetupTournamentPresenter = SetupTournamentPresenter() //This constructor might not be proper

    fun setTournamentType(type: TournamentType) {
        _presenter.tournamentType = type
    }

    fun getTeams(): List<Team> {
        return _presenter.teams
    }

    fun setTeams(teams: List<Team>) {
        _presenter.teams = teams as MutableList<Team>
    }

    private fun loadTeams(){

    }

    fun addTeam(team: Team) {
        _presenter.addTeam(team)
    }

    fun startTournament() {
        _presenter.startTournament()

    }

    fun isTournamentFull(): Boolean {
        return _presenter.isTournamentFull()
    }
    // TODO: Implement the ViewModel


}