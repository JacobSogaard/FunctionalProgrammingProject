package Presenters

import enums.TournamentType
import models.Game
import models.Team
import models.Tournament

class SetupTournamentPresenter {
    var tournamentType: TournamentType = TournamentType.SINGLEELIMINATION
    var tournament: Tournament? = null
    var teams = mutableListOf<Team>()

    fun addTeam(team: Team) {
        this.teams.add(team)
    }

    /*
    fun setTeamData(teams:MutableList<Team>) {
        teamsLive.value = teams
    }
    */

    fun startTournament() {
        tournament = Tournament(tournamentType, teams)
        tournament!!.start()

    }

    fun isTournamentFull(): Boolean {
        return teams.size < 128
    }
}