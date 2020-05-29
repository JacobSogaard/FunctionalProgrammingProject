package Adapters

import models.Team
import java.io.Serializable

class TeamsListHolder(var teams: List<Team>) : Serializable {
    val stringTeams = arrayListOf<ArrayList<String>>()

    init {
        teams.forEach{
            val tempList = ArrayList<String>()
            tempList.add(it.teamName)
            tempList.add(it.player1.name)
            tempList.add(it.player2.name)
            stringTeams.add(tempList)
        }
    }
}