package models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

//Should maybe be serializable
class Game(val team1: Team?, val team2: Team?, val id: Int)  {
    private  var winTeam:Team? = null
    private  var loseTeam:Team? = null
    private var team1Score = 0
    private  var team2Score:Int = 0
    private  var endRound:Int = 0

    fun setTeamScore(team: Team, score: Int) {
        if (this.team1 == team) team1Score = score else if (team2 == team) this.team2Score = score
    }

    fun getTeamScore(team: Team): Int {
        if (this.team1 == team) return team1Score else if (team2 == team) return this.team2Score
        return -1
    }

    fun getEndRound(): Int {
        return this.endRound
    }

    fun setMatchWinner(winnerTeam: Team, round: Int) {
        if (this.team1 == winnerTeam) {
            this.winTeam = this.team1
            this.loseTeam = team2
        } else if (team2 == winnerTeam) {
            this.winTeam = team2
            this.loseTeam = this.team1
        }
        this.endRound = round
    }

    fun getWinner(): Team? {
        return if (this.winTeam == null) {
            if (team1Score > this.team2Score) this.team1 else if (this.team2Score > team1Score) team2 else null
        } else null
    }

    fun getLoser(): Team? {
        if (this.winTeam == null) {
            return if (team1Score < this.team2Score) this.team1 else if (this.team2Score < team1Score) team2 else null
        } else if (this.winTeam == this.team1) return team2 else if (this.winTeam == team2) return this.team1
        return null
    }

    fun isWinner(team: Team): Boolean {
        return this.winTeam == team
    }


    /*
    override fun toString(): String? {
        val str: java.lang.StringBuilder = java.lang.StringBuilder()
        val teamNames: String =
            java.lang.String.format("%1$1s%2$40s", this.winTeam.getName(), this.loseTeam.getName())
        val scores: String =
            String.format("%1$20s%2$50s", getTeamScore(this.winTeam), getTeamScore(this.loseTeam))
        //String teamNames = this.winTeam.getName() + "\t\t\t" + this.loseTeam.getName();
        //String scores = this.getTeamScore(this.winTeam) + "\t\t\t" + this.getTeamScore(this.loseTeam);
        str.append(teamNames)
            .append("\n")
            .append(scores)
        return String(str)
    }

     */
}