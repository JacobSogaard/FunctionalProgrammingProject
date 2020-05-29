package models


import com.example.tournamentandroid.models.ITournament
import enums.TournamentType
import java.io.Serializable
import kotlin.random.Random


class Tournament(type: TournamentType, var teams: List<Team>) : Serializable, ITournament{

    var games = mutableListOf<Game>()
    private var isRandom: Boolean? = null
    private var currentRound = 0
    private  var maxRounds:Int = 0
    private  var firstRoundMatches:Int = 0
    private  var cupLimit:Int = 0
    private var onGoingMatches: MutableList<Game>? = null
    private  var concludedMatches:MutableList<Game>? = null
    private var teamsPickedThisRound: MutableSet<Int>? = null


   init {
        isRandom = false
        currentRound = 1
        onGoingMatches = ArrayList()
        concludedMatches = ArrayList()
        teamsPickedThisRound = HashSet()
    }



    override fun getAllTeams(): List<Team> {
        return this.teams
    }

    override fun setRandom(bool: Boolean) {
        isRandom = bool
    }

    override fun setCupLimit(limit: Int) {
        this.cupLimit = limit
    }

    override fun getCupLimit(): Int {
        return this.cupLimit
    }

    override fun getConcludedMatches(): List<Game?>? {
        return this.concludedMatches
    }

    override fun start() {
        //Maybe change a boolean to be checked in fx addTeam and stuff, so that method is no longer doing anything
        val teams: Int = this.teams.size




        if (teams == 4) {
            this.maxRounds = 2
            this.firstRoundMatches = 2
        } else if (teams < 9) {
            this.maxRounds = 3
            this.firstRoundMatches = teams - 4
        } else if (teams < 17) {
            this.maxRounds = 4
            this.firstRoundMatches = teams - 8
        } else if (teams < 33) {
            this.maxRounds = 5
            this.firstRoundMatches = teams - 16
        } else if (teams < 4){
            this.maxRounds = 1
            this.firstRoundMatches;
        }
        loadMatchesForRound()
    }

    private fun log2(x: Int): Int {
        return (Math.log(x.toDouble()) / Math.log(2.0)).toInt() + 1
    }


    override fun getCurrentRound(): Int {
        return currentRound
    }

    override fun getMaxRounds(): Int {
        return this.maxRounds
    }

    /**
     * Should be called after tournament has ended and before each call on
     * getMatchesForRound(int round).
     * @param round
     * @return
     */
    override fun hasRound(round: Int): Boolean {
        return this.maxRounds >= round
    }

    /**
     * Should only be called if hasRound(int round) returns true
     * for the same round.
     * @param round
     * @return
     */
    override fun getMathcesForRound(round: Int): List<Game> {
        val theMatches: MutableList<Game> = ArrayList()
        for (m in this.concludedMatches!!) {
            if (m.getEndRound() === round) theMatches.add(m)
        }
        return theMatches
    }


    private fun loadMatchesForRound() {
        teamsPickedThisRound!!.clear()
        if (currentRound == 1) {
            for (i in 0 until this.firstRoundMatches) {
                onGoingMatches!!.add(Game(getNextTeam(), getNextTeam(), Random.nextInt(0, 100000)))
            }
        } else {
            for (i in 0 until this.teams.size / 2) {
                onGoingMatches!!.add(Game(getNextTeam(), getNextTeam(), Random.nextInt(0, 100000)))
            }
        }
    }

    private fun getNextTeam(): Team? {
        if (this.teams.isEmpty()) {
            return null
        }
        var index: Int
        do {
            index = Random.nextInt(this.teams.size)
        } while (teamsPickedThisRound!!.contains(index))
        teamsPickedThisRound!!.add(index)
        return this.teams[index]
    }

    override fun isDone(): Boolean? {
        return onGoingMatches?.isEmpty()
    }

    //FIIIIIIIIIX
    override fun getNextMatch(): Game? {
        val g = Game(Team("empty", Player("1",0), Player("2, 0")),Team("empty", Player("1",0), Player("2, 0")), 2)
        return if (onGoingMatches!!.isNotEmpty()) {
            onGoingMatches!![0]
        } else g
    }


    override fun concludeMatch(match: Game) {
        //Update the match with the new score
        for (i in onGoingMatches!!.indices) {
            if (onGoingMatches!![i].id == match.id) {
                onGoingMatches!!.removeAt(i)
                this.concludedMatches?.add(match)


                val rmteam = this.teams as MutableList
                rmteam.removeAt(teams.indexOf(match.getLoser()))
                teams = rmteam
                break
            }
        }
        setWinner(match.getWinner())
    }

    override fun setWinner(team: Team?) {
        if (onGoingMatches!!.isEmpty()) {
            currentRound++
            loadMatchesForRound()
        }
    }

    override fun get(): Int {
        return teams.size
    }

}